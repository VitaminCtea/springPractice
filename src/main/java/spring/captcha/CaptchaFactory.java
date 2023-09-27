package spring.captcha;

public class CaptchaFactory {
    private CaptchaFactory() {}
    public static Captcha createLineCaptcha(int width, int height, int letters) { return new LineCaptcha(width, height, letters); }
    public static Captcha createLineCaptcha(int width, int height, String code) { return new LineCaptcha(width, height, code); }
}
