package customer.statement.processor.utils;

public enum TestFileEnum {
    TEST_VALID_REQUEST,
    TEST_DUPLICATE_REFERENCE_REQUEST,
    TEST_EMPTY_REQUEST,
    TEST_DUPLICATE_REF_INCORRECT_BALANCE_REQUEST,
    TEST_INCORRECT_BALANCE_REQUEST,
    TEST_BAD_REQUEST;

    public String getFileName() {
        return this.name().replace("_", "-").toLowerCase() + ".json";
    }
}
