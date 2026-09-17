package rw344.truckpacker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

/**
 * A {@link Truck config} is added from input file to accomodate the YAML format as well
 * It is chosen based on the extention .extention with @Json Property being reused
 */

public class InputLoader {
    private final ObjectMapper mapper = new ObjectMapper();

    public TruckConfig load(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Input file not found: " + path);
        }
        ObjectMapper mapper = path.endsWith(".xml") ? new XmlMapper() : new ObjectMapper
        return mapper.readValue(file, TruckConfig.class);
    }

    private ObjectMapper selectMapper(String path) throws IOException {
        String lower = path.toLowerCase();
        if (lower.endsWith(".json")) {
            return jsonMapper;
        } else if (lower.endsWith(".yaml"))
    }
}

