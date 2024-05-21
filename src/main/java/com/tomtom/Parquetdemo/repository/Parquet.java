package com.tomtom.Parquetdemo.repository;

import com.tomtom.Parquetdemo.model.Change;
import lombok.SneakyThrows;
import org.apache.avro.reflect.ReflectData;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;
import org.apache.parquet.filter2.compat.FilterCompat;
import org.apache.parquet.filter2.predicate.FilterApi;
import org.apache.parquet.filter2.predicate.FilterPredicate;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.metadata.BlockMetaData;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.api.Binary;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class Parquet {

    final Configuration conf = new Configuration(false);

    public Parquet() {
        AvroReadSupport.setAvroReadSchema(conf, ReflectData.AllowNull.get().getSchema(Change.class));
    }

    public List<BlockMetaData> getRowGroups(final InputFile file)throws IOException{
        try (final ParquetFileReader reader = ParquetFileReader.open(file)) {
            return  reader.getRowGroups();
        }
    }

    public Stream<Change> stream(final InputFile file, long start, long end) throws IOException {
        return stream(file, start, end, FilterCompat.NOOP);
    }

    public Change stream(final InputFile file, String id) throws IOException {
        FilterPredicate predicate = FilterApi.eq(FilterApi.binaryColumn("id"), Binary.fromString(id));

            predicate = FilterApi.or(predicate, FilterApi.eq(FilterApi.binaryColumn("id"), Binary.fromString(id)));

        List<Change> list = stream(file, 0, Long.MAX_VALUE, FilterCompat.get(predicate)).toList();
        if(list.isEmpty()){
            return null;
        }else return list.get(0);
    }

    private Stream<Change> stream(final InputFile file, long start, long end, FilterCompat.Filter filter)
        throws IOException {
        final ReaderIterator<Change> it = new ReaderIterator<>(
            AvroParquetReader.<Change>builder(file)
                             .withDataModel(ReflectData.get())
                             .withConf(conf)
                             .withFileRange(start, end)
                             .withFilter(filter)
                             .build());
        return StreamSupport.stream(((Iterable<Change>)() -> it).spliterator(), false)
                            .onClose(it::close);
    }

    public static class ReaderIterator<T> implements Iterator<T> {

        private final ParquetReader<T> reader;
        private Optional<T> next;

        public ReaderIterator(final ParquetReader<T> reader) {
            this.reader = reader;
            this.next = readNext();
        }

        @SneakyThrows
        private Optional<T> readNext() {
            return Optional.ofNullable(reader.read());
        }

        @Override
        public boolean hasNext() {
            return next.isPresent();
        }

        @Override
        public T next() {
            if (hasNext()) {
                final T row = next.get();
                next = readNext();
                return row;
            }
            throw new NoSuchElementException();
        }

        @SneakyThrows
        public void close() {
            reader.close();
        }
    }
}
