package net.jlxxw.wechat.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.jlxxw.wechat.exception.ParamCheckException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorDescriptor;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;

/**
 * 调用微信接口参数检查
 * @author chunyang.leng
 * @date 2022-04-15 1:14 PM
 */
@Aspect
@Component
@Order(-1)
public class ParamCheckAOP {

    private static final ConstraintHelper constraintHelper = ConstraintHelper.forAllBuiltinConstraints();
    private static final ConstraintValidatorFactory factory = new ConstraintValidatorFactoryImpl();
    private Map<Class<? extends Annotation>, List<? extends ConstraintValidatorDescriptor<?>>> enabledBuiltinConstraints = new HashMap<>();


    @PostConstruct
    private void init(){
        Field field = ReflectionUtils.findField(ConstraintHelper.class, "enabledBuiltinConstraints");
        try {
            field.setAccessible(true);
            enabledBuiltinConstraints = (Map<Class<? extends Annotation>, List<? extends ConstraintValidatorDescriptor<?>>> )field.get(constraintHelper);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Pointcut("execution(public * net.jlxxw.wechat.function.*.*.*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void validation(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Method declaredMethod = joinPoint.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        Annotation[][] parameterAnnotations = declaredMethod.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object param = args[i];
            int length = parameterAnnotations[i].length;
            List<Class<?>> validatedSet = new LinkedList<>();
            Set<String> validResult = new HashSet<>();
            for (int j = 0; j < length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                if (annotation instanceof Validated) {
                    if (Objects.isNull(param)) {
                        //  自定义异常
                        throw new ParamCheckException("必填项不能为空");
                    }
                    Validated validated = (Validated) annotation;
                    Class<?>[] classList = validated.value();
                    validResult.addAll (valid(param, classList));
                    validatedSet = Arrays.asList(classList);
                }else {
                    // 支持 JSR 303 原生注解检查
                    try {
                        Class<? extends Annotation> annotationType = annotation.annotationType();
                        if (!enabledBuiltinConstraints.containsKey(annotationType)){
                            // 不在 JSR 303 监测范围内的，跳出
                            continue;
                        }
                        Field groups = ReflectionUtils.findField(annotationType, "groups");
                        if (Objects.nonNull(groups)){
                            // 支持分组检查
                            groups.setAccessible(true);
                            Class<?>[] groupArray = (Class<?>[])groups.get(annotation);
                            if (groupArray != null &&  !CollectionUtils.containsAny(validatedSet,Arrays.asList(groupArray))){
                                // 不在组内的，跳出
                                continue;
                            }
                        }
                        List<? extends ConstraintValidatorDescriptor<? extends Annotation>> descriptors = constraintHelper.getAllValidatorDescriptors(annotationType);
                        if (!CollectionUtils.isEmpty(descriptors)){

                            for (ConstraintValidatorDescriptor<? extends Annotation> descriptor : descriptors) {
                                ConstraintValidator validator = descriptor.newInstance(factory);
                                validator.initialize(annotation);
                                boolean valid = validator.isValid(param, null);
                                if (!valid){
                                    Method message = ReflectionUtils.findMethod(annotationType, "message");
                                    String invoke = (String)message.invoke(annotation);
                                    validResult.add(invoke);
                                }
                            }
                        }
                    }catch (Exception e){
                        // ignore
                    }
                }
                if (!CollectionUtils.isEmpty(validResult)){
                    String message = String.join("\n", validResult);
                    throw new ParamCheckException(message);
                }
            }
        }
    }

    private Set<String> valid(Object obj, Class[] clazz) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> validate = validator.validate(obj, clazz);
        Set<String> set = new HashSet<>();
        validate.forEach(o -> {
            String message = o.getMessage();
            if (StringUtils.isNotBlank(message)) {
                set.add(message);
            }
        });
        return set;
    }
}
