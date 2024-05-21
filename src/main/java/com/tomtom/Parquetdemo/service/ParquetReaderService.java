package com.tomtom.Parquetdemo.service;

import com.tomtom.Parquetdemo.model.Change;
import com.tomtom.Parquetdemo.model.Page;
import com.tomtom.Parquetdemo.repository.Parquet;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParquetReaderService {

    private final Parquet parquet;
    private final Configuration hadoopConf;
    private final PathFilter pathFilter = p -> !p.getName().startsWith("_") && !p.getName().endsWith(".crc");

    public <T> List<T> readParquetFile(String filePath, Class<T> clazz) {
        List<T> records = new ArrayList<>();
        Path path = new Path(filePath);

        Configuration configuration = new Configuration();
        try (ParquetReader<T> reader = AvroParquetReader.<T>builder(path).withConf(configuration).build()) {
            T record;
            while ((record = reader.read()) != null) {
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    public List<Change> getChanges(Path path, Page page) throws IOException {
        return parquet.stream(HadoopInputFile.fromPath( path,hadoopConf), page.getStart(), page.getEnd()).toList();
    }

    public List<Page> getPages(Path path) throws IOException {

       return  parquet.getRowGroups(HadoopInputFile.fromPath(path, hadoopConf)).stream().map(
            b -> new Page( b.getStartingPos(), b.getStartingPos() + b.getCompressedSize()))
               .sorted(Comparator.comparing(Page::getStart)).toList();

    }

    public Change getChangesByIds(String id,Path path) {
        try {
            return parquet.stream(HadoopInputFile.fromPath(path, hadoopConf), id);
        } catch (IOException e) {
            return null;
        }
    }
}
