package rw344.truckpacker;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class InputLoader {
    private final ObjectMapper mapper = new ObjectMapper();

    public TruckConfig load(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Input file not found: " + path);
        }
        ObjectMapper mapper = path.endsWith(".xml") ? new XmlMapper() : new ObjectMapper();
        return mapper.readValue(file, TruckConfig.class);
    }
}
