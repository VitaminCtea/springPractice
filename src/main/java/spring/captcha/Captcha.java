package spring.captcha;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public abstract class Captcha {
    protected int width;
    protected int height;
    protected boolean randomCreated;
    protected long startTime;

    @Setter protected Color background;
    @Setter protected Font font;
    @Getter protected String code;

    protected int letters;
    protected final int expirationTime = 60;
    protected final StringBuilder verificationCode = new StringBuilder();

    // 仅适用于批量写入文件的方法，外部不建议调用
    protected interface ILineCaptcha {
        byte[] generateVerificationCodeImage(
                String fileType,
                boolean useDefaultVerificationCode,
                StringBuilder verificationCode, String code
        ) throws IOException;
    }

    @Getter @Setter private String fileType;
    private static int verificationStatus = 0x00;
    private static final int VERIFICATION_PASSED = 0x11;
    private static final int VERIFICATION_CODE_ERROR_TIME_NOT_EXPIRED = 0x01;
    private static final int VERIFICATION_CODE_PASSED_TIME_EXPIRED = 0x10;

    private static final int ZERO_ASCII = 48;
    private static final int UPPERCASE_A_ASCII = 65;
    private static final int LOWERCASE_A_ASCII = 97;

    public Captcha(int width, int height) { this(width, height, null); }
    public Captcha(int width, int height, int letters) { this(width, height, null, letters); }
    public Captcha(int width, int height, String code) { this(width, height, code, 4); }
    public Captcha(int width, int height, String code, int letters) {
        this.width = width;
        this.height = height;
        this.background = Color.white;
        this.randomCreated = code == null;
        this.code = randomCreated ? createAllCode() : code;
        this.font = new Font(Font.SERIF, Font.PLAIN, (int) (height * 0.45));
        this.letters = letters;
        this.fileType = "png";
    }

    protected String createAllCode() { return createNumberCode() + createLowercaseLetterCode() + createUppercaseLetterCode(); }
    protected String createUppercaseLetterCode() { return createCode(26, UPPERCASE_A_ASCII); }
    protected String createLowercaseLetterCode() { return createCode(26, LOWERCASE_A_ASCII); }
    protected String createNumberCode() { return createCode(10, ZERO_ASCII); }
    protected String createCode(int end, int startAscii) {
        return IntStream.range(0, end)
                .mapToObj(x -> String.valueOf((char) (startAscii + x)))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    protected byte[] saveImageByteArray(BufferedImage bufferedImage, String fileType) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, fileType, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        startTime = System.currentTimeMillis();
        return bytes;
    }

    protected Color generateRandomColor() {
        int red = colorValueOne();
        int green = colorValueOne();
        int blue = colorValueOne();
        if (red == background.getRed() && green == background.getGreen() && blue == background.getBlue()) return generateRandomColor();
        return new Color(red, green, blue);
    }

    private int colorValueOne() { return ThreadLocalRandom.current().nextInt(255); }

    protected int getVerificationStatus(String userInputVerificationCode) {
        int deltaTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        boolean isVerificationCodeValid = deltaTime <= expirationTime;
        boolean isVerificationCodeEquals = getVerificationCode().equals(userInputVerificationCode);
        if (isVerificationCodeEquals && isVerificationCodeValid) return setAndGetVerificationStatus(VERIFICATION_PASSED);
        if (!isVerificationCodeValid) return setAndGetVerificationStatus(VERIFICATION_CODE_PASSED_TIME_EXPIRED);
        return setAndGetVerificationStatus(VERIFICATION_CODE_ERROR_TIME_NOT_EXPIRED);
    }

    private int setAndGetVerificationStatus(int status) { return (verificationStatus |= status); }

    protected String getVerificationMessage(String userInputVerificationCode) {
        switch (getVerificationStatus(userInputVerificationCode)) {
            case VERIFICATION_PASSED: return "校验成功！";
            case VERIFICATION_CODE_ERROR_TIME_NOT_EXPIRED: return "您输入的验证码不正确，请重新输入！";
            case VERIFICATION_CODE_PASSED_TIME_EXPIRED: return "验证码已过期，请重新获取！";
            default: return "验证码不能为空！";
        }
    }

    public String getVerificationCode() { return verificationCode.toString(); }

    public abstract byte[] generateVerificationCodeImage() throws IOException;
}
