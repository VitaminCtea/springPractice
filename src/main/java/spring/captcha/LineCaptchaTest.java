package spring.captcha;

public class LineCaptchaTest {
    public static void main(String[] args) {
        final int FILES_NUMBER = 12;
        Captcha captcha = CaptchaFactory.createLineCaptcha(300, 70, 5);
        byte[] bytes = captcha.generateVerificationCodeImage();
        System.out.println(captcha.getVerificationMessage("jiji"));
        System.out.println(captcha.getVerificationMessage(captcha.getVerificationCode()));

        WriteVerificationCodeImageHelper writeVerificationCodeImageHelper = new WriteVerificationCodeImageHelper(captcha, "captcha");
        String[] codes = new String[]{ "大吉大利", "大吉大利", "今晚吃鸡", "今晚吃鸡" };
        writeVerificationCodeImageHelper.setMaxFilesSavedNumber(FILES_NUMBER);
        writeVerificationCodeImageHelper.batchWriteVerificationCodeImagesToFile(FILES_NUMBER, codes);
//        writeVerificationCodeImageHelper.toFile(bytes);
    }
}
