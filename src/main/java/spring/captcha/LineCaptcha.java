package spring.captcha;

import lombok.Getter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LineCaptcha extends Captcha implements Captcha.ILineCaptcha {
    private final static Logger logger = Logger.getLogger("spring.captcha.LineCaptcha");
    static {
        logger.setUseParentHandlers(false);
    }
    private final int interfereCount = 5;
    @Getter private static class RecordLetter {
        private final String letter;
        private final double radian;
        private final int width;
        private final int height;
        RecordLetter(String letter, double radian, int width, int height) {
            this.letter = letter;
            this.radian = radian;
            this.width = width;
            this.height = height;
        }
    }
    public LineCaptcha(int width, int height) { super(width, height); }
    public LineCaptcha(int width, int height, int letters) { super(width, height, letters); }
    public LineCaptcha(int width, int height, String code) { super(width, height, code); }

    @Override public byte[] generateVerificationCodeImage() {
        return generateVerificationCodeImage(getFileType(), randomCreated, verificationCode, code);
    }

    @Override public byte[] generateVerificationCodeImage(
            String fileType,
            boolean useDefaultVerificationCode,
            StringBuilder verificationCode,
            String code
    ) {
        try {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bufferedImage.createGraphics();

            makeImageHighFidelityQuality(g);
            generateRoundedCanvas(g, bufferedImage);

            generateHotPixel(g);
            generateInterferenceLine(g);
            generateVerificationCode(g, useDefaultVerificationCode, verificationCode, code);
            g.dispose();

            return saveImageByteArray(bufferedImage, fileType);
        } catch (IOException e) { logger.severe(e.getMessage()); }

        return new byte[0];
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
        int letterTotalWidth = recordLetters.stream().mapToInt(RecordLetter::getWidth).sum();
        final int MARGIN = 20;
        int contentWidth = width - MARGIN;
        int remainingSpace = contentWidth - letterTotalWidth;
        int letterSpace = remainingSpace / verificationCode.length();

        int startLetterXPosition = 10;
        int length = Math.min(code.length(), letters);

        AffineTransform oldTransform = g.getTransform();

        for (int i = 0; i < length; i++) {
            RecordLetter letter = recordLetters.get(i);
            g.setPaint(generateRandomColor());
            g.translate(startLetterXPosition + letter.getWidth() / 2, height / 3 + letter.getHeight() / 3);
            g.rotate(letter.getRadian());
            g.drawString(letter.getLetter(), 0, 0);
            startLetterXPosition += letter.getWidth() + (i < length - 1 ? letterSpace : 0);
            g.setTransform(oldTransform);
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
            double r = (rand.nextInt(45) * Math.PI / 180) * (rand.nextBoolean() ? 1 : -1);
            double radian = Math.abs(r);
            double width = textWidth * Math.cos(radian) + textHeight * Math.sin(radian);
            double height = textHeight * Math.cos(radian) + textWidth * Math.sin(radian);
            verificationCode.append(letter);
            recordLetters.add(new RecordLetter(letter, r, (int) width, (int) height));
        }

        return recordLetters;
    }
}
