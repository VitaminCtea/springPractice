package spring.captcha;

import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class LineCaptcha extends Captcha implements Captcha.ILineCaptcha {
    private final static Logger logger = Logger.getLogger("spring.captcha.LineCaptcha");
    private Random rand;
    static {
        logger.setUseParentHandlers(false);
    }

    @Getter private static class RecordLetter {
        private final String letter;
        private final double radian;
        private final double width;
        private final double height;
        RecordLetter(String letter, double radian, double width, double height) {
            this.letter = letter;
            this.radian = radian;
            this.width = width;
            this.height = height;
        }
    }
    public LineCaptcha(int width, int height) { super(width, height); }
    public LineCaptcha(int width, int height, int letters) { super(width, height, letters); }
    public LineCaptcha(int width, int height, String code) { super(width, height, code); }

    @Override public byte[] generateVerificationCodeImage() throws IOException {
        return generateVerificationCodeImage(getFileType(), randomCreated, verificationCode, code);
    }

    @Override public byte[] generateVerificationCodeImage(
            String fileType,
            boolean useDefaultVerificationCode,
            StringBuilder verificationCode,
            String code
    ) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        rand = ThreadLocalRandom.current();

        makeImageHighFidelityQuality(g);
        generateRoundedCanvas(g, bufferedImage);

        generateHotPixel(g);
        generateInterferenceLine(g);
        generateVerificationCode(g, useDefaultVerificationCode, verificationCode, code);

        g.dispose();
        return saveImageByteArray(bufferedImage, fileType);
    }

    private void makeImageHighFidelityQuality(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void generateRoundedCanvas(Graphics2D g, BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        g.fillRoundRect(0, 0, width, height, 20, 20);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(bufferedImage, 0, 0, width, height, null);
    }

    private void generateHotPixel(Graphics2D g) {
        generatorInterference(
                g,
                width * 2,
                () -> g.fillOval(rand.nextInt(width), rand.nextInt(height), 2, 2));
    }

    private void generateInterferenceLine(Graphics2D g) {
        int interfereCount = 5;
        generatorInterference(
                g,
                interfereCount,
                () -> g.drawLine(rand.nextInt(width), rand.nextInt(height), rand.nextInt(width), rand.nextInt(height)));
    }

    private void generatorInterference(Graphics2D g, int end, Runnable gen) {
        for (int i = 0; i < end; i++) {
            g.setPaint(generateRandomColor());
            gen.run();
        }
    }

    private void generateVerificationCode(Graphics2D g, boolean useDefaultVerificationCode, StringBuilder verificationCode, String code) {
        g.setFont(font);

        List<RecordLetter> recordLetters = recordLetterAttribute(g, useDefaultVerificationCode, verificationCode, code);
        double letterTotalWidth = recordLetters.stream().mapToDouble(RecordLetter::getWidth).sum();
        double remainingSpace = (width - letterTotalWidth) / 2;
        final int VERIFICATION_CODE_LENGTH = verificationCode.length();
        final int NEED_USE_SPACE_COUNT = VERIFICATION_CODE_LENGTH - 1;
        final double MARGIN = remainingSpace / 2 / NEED_USE_SPACE_COUNT;
        double totalWidth = letterTotalWidth + MARGIN * NEED_USE_SPACE_COUNT;
        double letterHeight = g.getFontMetrics(font).getHeight();
        double startLetterXPosition = (width - totalWidth) / 2;

        int length = Math.min(code.length(), letters);
        AffineTransform oldTransform = g.getTransform();

        for (int i = 0; i < length; i++) {
            RecordLetter letter = recordLetters.get(i);
            g.setPaint(generateRandomColor());
            g.translate(startLetterXPosition, (double) height / 3 + letterHeight / 2);
            g.rotate(letter.getRadian());
            g.drawString(letter.getLetter(), 0, 0);
            g.setTransform(oldTransform);
            startLetterXPosition += letter.getWidth() + (i < length - 1 ? MARGIN : 0);
        }
    }

    private List<RecordLetter> recordLetterAttribute(
            Graphics2D g,
            boolean useDefaultVerificationCode,
            StringBuilder verificationCode,
            String code
    ) {
        List<RecordLetter> recordLetters = new ArrayList<>();
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int textHeight = fontMetrics.getHeight();

        int length = Math.min(code.length(), letters);
        for (int i = 0; i < length; i++) {
            String letter = String.valueOf(code.charAt(useDefaultVerificationCode ? rand.nextInt(code.length()) : i));
            int textWidth = fontMetrics.stringWidth(letter);
            double r = rand.nextInt(45) * (rand.nextBoolean() ? 1 : -1) * Math.PI / 180;
            double radian = Math.abs(r);
            double afterRotateWidth = textWidth * Math.cos(radian) + textHeight * Math.sin(radian);
            double afterRotateHeight = textWidth * Math.sin(radian) + textHeight * Math.cos(radian);
            verificationCode.append(letter);
            recordLetters.add(new RecordLetter(letter, r, afterRotateWidth, afterRotateHeight));
        }

        return recordLetters;
    }
}
