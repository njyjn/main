package seedu.cmdo.storage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.cmdo.commons.core.LogsCenter;
import seedu.cmdo.commons.exceptions.DataConversionException;
import seedu.cmdo.commons.util.FileUtil;
import seedu.cmdo.model.UserPrefs;

/**
 * A class to access UserPrefs stored in the hard disk as a json file
 */
public class JsonUserPrefStorage implements UserPrefsStorage{

    private static final Logger logger = LogsCenter.getLogger(JsonUserPrefStorage.class);

    private String filePath;

    public JsonUserPrefStorage(String filePath){
        this.filePath = filePath;
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return readUserPrefs(filePath);
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        saveUserPrefs(userPrefs, filePath);
    }

    /**
     * Similar to {@link #readUserPrefs()}
     * @param prefsFilePath location of the data. Cannot be null.
     * @throws DataConversionException if the file format is not as expected.
     */
    public Optional<UserPrefs> readUserPrefs(String prefsFilePath) throws DataConversionException {
        assert prefsFilePath != null;

        File prefsFile = new File(prefsFilePath);

        if (!prefsFile.exists()) {
            logger.info("Prefs file "  + prefsFile + " not found");
            return Optional.empty();
        }

        UserPrefs prefs;

        try {
            prefs = FileUtil.deserializeObjectFromJsonFile(prefsFile, UserPrefs.class);
        } catch (IOException e) {
            logger.warning("Error reading from prefs file " + prefsFile + ": " + e);
            throw new DataConversionException(e);
        }

        return Optional.of(prefs);
    }

    /**
     * Similar to {@link #saveUserPrefs(UserPrefs)}
     * @param prefsFilePath location of the data. Cannot be null.
     */
    public void saveUserPrefs(UserPrefs userPrefs, String prefsFilePath) throws IOException {
        assert userPrefs != null;
        assert prefsFilePath != null;

        FileUtil.serializeObjectToJsonFile(new File(prefsFilePath), userPrefs);
    }
}
