package com.gestaopsi.prd.service;

import com.gestaopsi.prd.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.max-size:10485760}") // 10MB default
    private long maxFileSize;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    /**
     * Salva arquivo no sistema de arquivos
     */
    public String storeFile(MultipartFile file, String subDirectory) {
        validateFile(file);

        try {
            // Cria diretório se não existir
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            Files.createDirectories(uploadPath);

            // Gera nome único para o arquivo
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
            String filename = UUID.randomUUID().toString() + extension;

            // Salva o arquivo
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Arquivo salvo: {}", filePath);
            return filename;

        } catch (IOException e) {
            log.error("Erro ao salvar arquivo", e);
            throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    /**
     * Salva imagem com compressão automática
     */
    public String storeImage(MultipartFile file, String subDirectory, int maxWidth) {
        validateImageFile(file);

        try {
            // Lê a imagem
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new InvalidDataException("Não foi possível processar a imagem");
            }

            // Redimensiona se necessário
            BufferedImage resizedImage = resizeImage(originalImage, maxWidth);

            // Converte para bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String formatName = getImageFormat(file.getContentType());
            ImageIO.write(resizedImage, formatName, baos);
            byte[] imageBytes = baos.toByteArray();

            // Cria diretório
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            Files.createDirectories(uploadPath);

            // Gera nome único
            String filename = UUID.randomUUID().toString() + "." + formatName;
            Path filePath = uploadPath.resolve(filename);

            // Salva arquivo
            Files.write(filePath, imageBytes);

            log.info("Imagem comprimida e salva: {}", filePath);
            return filename;

        } catch (IOException e) {
            log.error("Erro ao processar imagem", e);
            throw new RuntimeException("Erro ao processar imagem: " + e.getMessage());
        }
    }

    /**
     * Carrega arquivo
     */
    public byte[] loadFile(String filename, String subDirectory) {
        try {
            Path filePath = Paths.get(uploadDir, subDirectory, filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Erro ao carregar arquivo: {}", filename, e);
            throw new RuntimeException("Arquivo não encontrado");
        }
    }

    /**
     * Deleta arquivo
     */
    public void deleteFile(String filename, String subDirectory) {
        try {
            Path filePath = Paths.get(uploadDir, subDirectory, filename);
            Files.deleteIfExists(filePath);
            log.info("Arquivo deletado: {}", filePath);
        } catch (IOException e) {
            log.error("Erro ao deletar arquivo: {}", filename, e);
        }
    }

    /**
     * Valida arquivo
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidDataException("Arquivo está vazio");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidDataException(
                String.format("Arquivo muito grande. Máximo: %d MB", maxFileSize / 1024 / 1024)
            );
        }

        String contentType = file.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType) && !ALLOWED_DOCUMENT_TYPES.contains(contentType)) {
            throw new InvalidDataException("Tipo de arquivo não permitido: " + contentType);
        }
    }

    /**
     * Valida arquivo de imagem
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidDataException("Imagem está vazia");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidDataException("Imagem muito grande");
        }

        String contentType = file.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new InvalidDataException("Tipo de imagem não permitido: " + contentType);
        }
    }

    /**
     * Redimensiona imagem mantendo proporção
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth <= maxWidth) {
            return originalImage;
        }

        int newWidth = maxWidth;
        int newHeight = (originalHeight * maxWidth) / originalWidth;

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

    /**
     * Obtém formato da imagem baseado no content type
     */
    private String getImageFormat(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }
}


