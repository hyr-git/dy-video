package com.shuyao.image.video;

import lombok.var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VideoCreator extends JFrame {

    private JTextField imageFolderField;
    private JTextField audioFileField;
    private JTextField outputField;
    private JComboBox<String> frameRateCombo;
    private JComboBox<String> resolutionCombo;
    private JComboBox<String> formatCombo;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;
    private Process ffmpegProcess;
    private boolean isProcessing = false;

    public VideoCreator() {
        setTitle("图集视频生成器");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        imageFolderField = new JTextField();
        JButton browseImageBtn = new JButton("浏览...");
        browseImageBtn.addActionListener(e -> browseFolder(imageFolderField));
        
        audioFileField = new JTextField();
        JButton browseAudioBtn = new JButton("浏览...");
        browseAudioBtn.addActionListener(e -> browseFile(audioFileField, "音频文件"));
        
        outputField = new JTextField();
        JButton browseOutputBtn = new JButton("浏览...");
        browseOutputBtn.addActionListener(e -> browseOutputFile(outputField));
        
        frameRateCombo = new JComboBox<>(new String[]{"15", "24", "30", "60"});
        frameRateCombo.setSelectedIndex(2);
        
        resolutionCombo = new JComboBox<>(new String[]{
            "480p (854x480)", 
            "720p (1280x720)", 
            "1080p (1920x1080)", 
            "2K (2560x1440)", 
            "4K (3840x2160)"
        });
        resolutionCombo.setSelectedIndex(1);
        
        formatCombo = new JComboBox<>(new String[]{".mp4", ".mov", ".avi"});
        
        inputPanel.add(new JLabel("图片文件夹:"));
        inputPanel.add(createFieldWithButton(imageFolderField, browseImageBtn));
        inputPanel.add(new JLabel("音频文件 (可选):"));
        inputPanel.add(createFieldWithButton(audioFileField, browseAudioBtn));
        inputPanel.add(new JLabel("输出文件:"));
        inputPanel.add(createFieldWithButton(outputField, browseOutputBtn));
        inputPanel.add(new JLabel("帧率 (FPS):"));
        inputPanel.add(frameRateCombo);
        inputPanel.add(new JLabel("分辨率:"));
        inputPanel.add(resolutionCombo);
        inputPanel.add(new JLabel("输出格式:"));
        inputPanel.add(formatCombo);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        startButton = new JButton("开始生成");
        startButton.addActionListener(e -> startProcessing());
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        
        stopButton = new JButton("停止");
        stopButton.addActionListener(e -> stopProcessing());
        stopButton.setEnabled(false);
        stopButton.setBackground(new Color(220, 20, 60));
        stopButton.setForeground(Color.WHITE);
        
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // 进度条
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(50, 205, 50));

        // 日志区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("处理日志"));

        // 组装主界面
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.NORTH);
        add(logScrollPane, BorderLayout.CENTER);

        // 设置默认输出文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultName = "video_" + sdf.format(new Date()) + ".mp4";
        outputField.setText(System.getProperty("user.home") + File.separator + defaultName);
    }

    private JPanel createFieldWithButton(JTextField field, JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(field, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }

    private void browseFolder(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browseFile(JTextField field, String description) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择" + description);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                description + " (*.mp3, *.wav)", "mp3", "wav"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browseOutputFile(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("保存视频文件");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "视频文件 (*.mp4, *.mov, *.avi)", "mp4", "mov", "avi"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String selectedFile = chooser.getSelectedFile().getAbsolutePath();
            // 确保文件扩展名
            String format = (String) formatCombo.getSelectedItem();
            if (format != null && !selectedFile.toLowerCase().endsWith(format)) {
                selectedFile += format;
            }
            field.setText(selectedFile);
        }
    }

    private void startProcessing() {
        if (isProcessing) return;
        
        String imageFolder = imageFolderField.getText().trim();
        String audioFile = audioFileField.getText().trim();
        String outputFile = outputField.getText().trim();
        
        if (imageFolder.isEmpty() || outputFile.isEmpty()) {
            log("错误：请选择图片文件夹和输出文件");
            return;
        }
        
        File folder = new File(imageFolder);
        if (!folder.exists() || !folder.isDirectory()) {
            log("错误：图片文件夹不存在或无效");
            return;
        }
        
        // 检查FFmpeg
        if (!checkFFmpeg()) {
            log("错误：FFmpeg未安装或未在系统路径中找到");
            log("请访问 https://ffmpeg.org 下载并安装FFmpeg");
            return;
        }
        
        // 获取图片文件
        File[] imageFiles = folder.listFiles((dir, name) -> 
                name.toLowerCase().matches(".*\\.(jpg|jpeg|png|bmp)"));
        
        if (imageFiles == null || imageFiles.length == 0) {
            log("错误：未找到支持的图片文件 (jpg, jpeg, png, bmp)");
            return;
        }
        
        // 按文件名排序
        Arrays.sort(imageFiles, Comparator.comparing(File::getName));
        
        log("找到 " + imageFiles.length + " 张图片");
        
        // 创建临时目录
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("video_creator_");
            log("创建临时目录: " + tempDir);
        } catch (IOException e) {
            log("创建临时目录失败: " + e.getMessage());
            return;
        }
        
        // 重命名图片为序列格式
        log("重命名图片为序列格式...");
        for (int i = 0; i < imageFiles.length; i++) {
            String newName = String.format("img_%04d%s", i + 1, 
                    getExtension(imageFiles[i].getName()));
            Path dest = tempDir.resolve(newName);
            try {
                Files.copy(imageFiles[i].toPath(), dest);
            } catch (IOException e) {
                log("复制图片失败: " + e.getMessage());
                return;
            }
        }
        
        // 构建FFmpeg命令
        String frameRate = (String) frameRateCombo.getSelectedItem();
        String resolution = ((String) resolutionCombo.getSelectedItem()).split("\\s+")[1];
        String format = (String) formatCombo.getSelectedItem();
        
        StringBuilder cmd = new StringBuilder("ffmpeg -y -framerate " + frameRate + 
                " -i \"" + tempDir.toString() + File.separator + "img_%04d.png\"");
        
        if (!audioFile.isEmpty()) {
            File audio = new File(audioFile);
            if (audio.exists()) {
                cmd.append(" -i \"").append(audioFile).append("\"");
            } else {
                log("警告：音频文件不存在，继续生成无音频视频");
            }
        }
        
        cmd.append(" -s ").append(resolution)
          .append(" -c:v libx264 -pix_fmt yuv420p")
          .append(" -preset medium -crf 23");
        
        if (!audioFile.isEmpty() && new File(audioFile).exists()) {
            cmd.append(" -c:a aac -b:a 192k -shortest");
        }
        
        cmd.append(" \"").append(outputFile).append("\"");
        
        log("执行命令: " + cmd);
        
        // 更新UI状态
        isProcessing = true;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        progressBar.setValue(0);
        
        // 启动FFmpeg进程
        new Thread(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", cmd.toString());
                } else {
                    builder.command("sh", "-c", cmd.toString());
                }
                
                builder.redirectErrorStream(true);
                ffmpegProcess = builder.start();
                
                // 监控进度
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleAtFixedRate(() -> updateProgress(tempDir, imageFiles.length), 
                        1, 1, TimeUnit.SECONDS);
                
                // 读取输出
                try (var reader = new java.io.BufferedReader(
                     new java.io.InputStreamReader(ffmpegProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log(line);
                    }
                }
                
                int exitCode = ffmpegProcess.waitFor();
                scheduler.shutdown();
                
                if (exitCode == 0) {
                    log("视频生成成功！");
                    progressBar.setValue(100);
                    JOptionPane.showMessageDialog(this, 
                            "视频生成成功！\n保存位置: " + outputFile, 
                            "完成", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    log("错误：FFmpeg处理失败，退出代码: " + exitCode);
                }
                
                // 清理临时目录
                try {
                    Files.walk(tempDir)
                         .sorted(Comparator.reverseOrder())
                         .map(Path::toFile)
                         .forEach(File::delete);
                    log("临时目录已清理");
                } catch (IOException e) {
                    log("清理临时目录失败: " + e.getMessage());
                }
                
            } catch (IOException | InterruptedException e) {
                log("处理失败: " + e.getMessage());
            } finally {
                isProcessing = false;
                SwingUtilities.invokeLater(() -> {
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                });
            }
        }).start();
    }

    private void stopProcessing() {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            ffmpegProcess.destroy();
            log("处理已停止");
        }
        isProcessing = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void updateProgress(Path tempDir, int totalImages) {
        if (!isProcessing) return;
        
        try {
            long processed = Files.list(tempDir)
                                 .filter(p -> p.toString().endsWith(".processed"))
                                 .count();
            
            int progress = (int) ((double) processed / totalImages * 100);
            SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
        } catch (IOException e) {
            log("更新进度失败: " + e.getMessage());
        }
    }

    private boolean checkFFmpeg() {
        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -version");
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? ".png" : filename.substring(dotIndex);
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            VideoCreator app = new VideoCreator();
            app.setVisible(true);
        });
    }
}