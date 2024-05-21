package com.tomtom.Parquetdemo.service;

import com.tomtom.Parquetdemo.model.Change;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;


public class ParquetWriterService {


    public void write(final Dataset<Change> changes, final Path path) {
        Path fullPath = new Path("testPath");
        changes.cache().sort(changes.col("id")).repartition(1).write()
               .option("parquet.block.size", 16777216).mode(SaveMode.Overwrite)
               .parquet(fullPath.toString());
    }
}
