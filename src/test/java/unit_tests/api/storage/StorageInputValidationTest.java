package unit_tests.api.storage;

import de.storagesystem.api.storage.StorageInputValidation;
import de.storagesystem.api.storage.StorageInputValidationImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StorageInputValidationTest {
    static StorageInputValidation inputValidation;

    @BeforeAll
    public static void init() {
        inputValidation = new StorageInputValidationImpl();
    }

    /**
     * Test if the folder name input validation works
     */
    @Test
    public void testFilePathValidation() {
        assertFalse(inputValidation.validateFilePath(""));
        assertFalse(inputValidation.validateFilePath(null));
        assertFalse(inputValidation.validateFilePath("ThisIsNotAValidFilePath/test.txt/"));
        assertFalse(inputValidation.validateFilePath("/ThisIsNotAValidFilePath/test.txt"));

        assertTrue(inputValidation.validateFilePath("ThisIsAValidFilePath/test.txt"));
    }


}
