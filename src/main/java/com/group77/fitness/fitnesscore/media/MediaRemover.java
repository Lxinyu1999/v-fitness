package com.group77.fitness.fitnesscore.media;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

// 从数据目录中删除媒体文件
public class MediaRemover {
    public static void deleteMedia(MediaDirectory mediaDirectory, String ID) throws IOException {  // IOException from FileUtils.forceDelete()
        // 按 prefix (basename) 找到相应的文件，然后获取其后缀名
        Collection<File> listFiles = FileUtils.listFiles(new File(mediaDirectory.getPath()),
                FileFilterUtils.prefixFileFilter(ID), null);
        String fileExtension = FilenameUtils.getExtension(listFiles.iterator().next().getName());

        FileUtils.forceDelete(new File(mediaDirectory.getPath() + ID + '.' + fileExtension));
    }
}
