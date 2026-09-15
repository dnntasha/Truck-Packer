package rw344.truckpacker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

/**
 * Loads a truckconfig from an input file, this supports the JSON and YAML formata
 * it's chosen based on the extention (.extention)
 * YAML uses the same @JsonProperties that JSON already relies on
 */
public class InputLoader {
    
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper yamlMapper = new YAMLMapper();

    /**
     * @param path to the input file must end in .json, .yaml or .yml.
     * @return the parsed truck configuration
     * @throws IOException if the file is nonexistent, the extention is unsupported or as they say, "unparsable".
     */
    public TruckConfig load(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Input file not found: " + path);
        }
        ObjectMapper mapper = selectMapper(path);
        return mapper.readValue(file, TruckConfig.class);
    }

    /**
     * Picks the relevent Jackson mapper 
     * @return the mapper for parsing
     * @throws IOException if the extention is unsupported
     */
    private ObjectMapper selectMapper(String path) throws IOException {
        String lower = path.toLowerCase();
        if (lower.endsWith(".json")) {
            return jsonMapper;
        } else if (lower.endsWith(".yaml") || lower.endsWith(".yml")) {
            return yamlMapper;
        } else {
            throw new IOException("Unsupported input file extension for '" + path + "'. Supported formats are: .json .yaml/.yml");
        }
    }
}
