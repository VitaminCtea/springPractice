package spring.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "哎呀，出错了啊！")
public class CustomFileAlreadyExistsException extends RuntimeException {}