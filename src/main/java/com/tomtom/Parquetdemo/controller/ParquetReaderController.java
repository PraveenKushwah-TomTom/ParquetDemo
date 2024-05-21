package com.tomtom.Parquetdemo.controller;

import com.tomtom.Parquetdemo.model.Change;
import com.tomtom.Parquetdemo.model.ChangesResponse;
import com.tomtom.Parquetdemo.model.Cursor;
import com.tomtom.Parquetdemo.model.Page;
import com.tomtom.Parquetdemo.service.ParquetReaderService;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RawLocalFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.apache.hadoop.conf.Configuration;

@RestController
public class ParquetReaderController {

    @Autowired
    private ParquetReaderService parquetReaderService;

    public static final String CURSOR_QUERY_PARAM = "cursor";

    @GetMapping("/change")
    public ResponseEntity<Change> getChange(@RequestParam String filePath, @RequestParam String id) throws IOException {

        Change change = parquetReaderService.getChangesByIds(id,buildPath(filePath));
        if(change==null){
            return ResponseEntity.notFound().build();
        }else
        return ResponseEntity.ok().body(change);
    }


    @GetMapping("/read")
    public ResponseEntity<ChangesResponse> readParquet(@RequestParam String filePath,
                                                       @RequestParam(name = CURSOR_QUERY_PARAM, required = false) String cursor)
        throws IOException {
        // Replace Object with the actual class type you expect to read from the Parquet file
        ChangesResponse.ChangesResponseBuilder response = ChangesResponse.builder()
                                                                         .self(buildURI(Optional.empty()));
        Cursor self;
        if (cursor != null && !cursor.isEmpty()) {
            self = new Cursor(Integer.valueOf(cursor));
        }else{
            self = Cursor.getFirst();
        }

        Path path = buildPath(filePath);
        List<Page> pages = parquetReaderService.getPages(path);
        response.cursors(Cursor.toCursorList(pages));
        if(pages.size() > self.getCurrentIndex()){
            response.changes(parquetReaderService.getChanges(path,pages.get(self.getCurrentIndex())));
        }
        Optional<Cursor> next = self.getNext(pages);
        next.ifPresent(value -> response.next(buildURI(next)));
        Optional<Cursor> prev = self.getPrev(pages);
        prev.ifPresent(value -> response.prev(buildURI(prev)));

        return ResponseEntity.ok().body(response.build());
    }

    private URI buildURI(final Optional<Cursor> cursor) {
        final UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        cursor.ifPresent(value -> uriBuilder.replaceQueryParam(CURSOR_QUERY_PARAM,value.getCurrentIndex()));
        return uriBuilder.build(true).toUri();
    }

    private Path buildPath(String filePath){
        ClassPathResource resource = new ClassPathResource(filePath);
        Configuration configuration = new Configuration();

        // Use a RawLocalFileSystem to handle InputStream directly
        RawLocalFileSystem fs = new RawLocalFileSystem();
        fs.setConf(configuration);
        Path path=null;
        try (InputStream is = resource.getInputStream();) {
            path = new Path(resource.getURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
