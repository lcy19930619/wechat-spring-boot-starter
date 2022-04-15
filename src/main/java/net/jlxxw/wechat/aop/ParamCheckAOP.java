package net.jlxxw.wechat.aop;

import net.jlxxw.wechat.exception.ParamCheckException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * 调用微信接口参数检查
 * @author chunyang.leng
 * @date 2022-04-15 1:14 PM
 */
@Aspect
@Component
@Order(-1)
public class ParamCheckAOP {

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
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
            for (int j = 0; j < length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                if (annotation instanceof Validated) {
                    if (Objects.isNull(param)) {
                        //  自定义异常
                        throw new ParamCheckException("必填项不能为空");
                    }
                    Validated validated = (Validated) annotation;
                    Class<?>[] classList = validated.value();
                    String validResult = valid(param, classList);
                    if (StringUtils.isNotEmpty(validResult)) {
                        //  自定义异常
                        throw new ParamCheckException(validResult);
                    }
                }
            }
        }
    }

    private String valid(Object obj, Class[] clazz) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> validate = validator.validate(obj, clazz);
        StringBuffer stringBuffer = new StringBuffer();
        validate.forEach(o -> {
            String message = o.getMessage();
            if (StringUtils.isNotBlank(message)) {
                stringBuffer.append(message).append("\n");
            }
        });
        return stringBuffer.toString();
    }
}
