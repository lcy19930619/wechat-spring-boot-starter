package net.jlxxw.wechat.exception;

/**
 * 参数检查失败，抛出的异常
 *
 * @author chunyang.leng
 * @date 2022-04-15 1:15 PM
 */
public class ParamCheckException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ParamCheckException(String message) {
        super(message);
    }
}
