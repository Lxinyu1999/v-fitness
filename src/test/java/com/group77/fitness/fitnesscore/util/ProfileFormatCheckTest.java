package com.group77.fitness.fitnesscore.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.Calendar;
import java.util.List;


public class ProfileFormatCheckTest {
    ProfileFormatChecker profileFormatChecker;

    @BeforeEach
    public void setUp() throws Exception {
        this.profileFormatChecker = new ProfileFormatChecker();
    }

    @AfterEach
    public void tearDown() {
        this.profileFormatChecker = null;
    }

    @ParameterizedTest
    @MethodSource
    void testCheckAccountID(String accountID, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkAccountID(null, accountID));
    }
    static List<Arguments> testCheckAccountID() {
        return List.of(
                Arguments.arguments("", false),
                Arguments.arguments("               ", false),
                Arguments.arguments("A", false),
                Arguments.arguments("Boss", false),
                Arguments.arguments("Alex", false),
                Arguments.arguments("Mike", false),
                Arguments.arguments("ABC", true),
                Arguments.arguments("123456", true),
                Arguments.arguments("ABC-123", false),
                Arguments.arguments("ABC_123", false),
                Arguments.arguments("ABC 123", false),
                Arguments.arguments("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789", false)
                );
    }

    @ParameterizedTest
    @MethodSource
    void testCheckPassword(String password, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkPassword(null, password));

    }
    static List<Arguments> testCheckPassword() {
        return List.of(
                Arguments.arguments("", false),
                Arguments.arguments("abc", false),
                Arguments.arguments("abcdefghijk", false),
                Arguments.arguments("123456789", false),
                Arguments.arguments("abc123456", true),
                Arguments.arguments("ABC123456", true),
                Arguments.arguments("123456ABC", true),
                Arguments.arguments("123456-ABC", true),
                Arguments.arguments("123456_ABC", true),
                Arguments.arguments("123456 ABC", true),
                Arguments.arguments("一", false),
                Arguments.arguments("一二三四", false),
                Arguments.arguments("一二三四五六七八", false),
                Arguments.arguments("A1一二三四五六七八", true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCheckConfirmedPassword(String password, String confirmedPassword, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkConfirmedPassword(null, password, confirmedPassword));
    }

    static List<Arguments> testCheckConfirmedPassword() {
        return List.of(
                Arguments.arguments("", "", false),
                Arguments.arguments("A", "A", false),
                Arguments.arguments("ABC", "ABC", false),
                Arguments.arguments("1", "1", false),
                Arguments.arguments("ABCDEFGHIJK", "ABCDEFGHIJK", false),
                Arguments.arguments("123456789", "123456789", false),
                Arguments.arguments("ABC12345", "abc12345", false),
                Arguments.arguments("ABC12345", "AB12345", false),
                Arguments.arguments("ABC12345", "ABC12345", true),
                Arguments.arguments("ABC12345", "ABC12345", true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCheckBirthYear(String birthYear, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkBirthYear(null, birthYear));
    }
    static List<Arguments> testCheckBirthYear() {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        return List.of(
                Arguments.arguments("", false),
                Arguments.arguments("ABC", false),
                Arguments.arguments("100", false),
                Arguments.arguments("100ABC", false),
                Arguments.arguments("10A0BC", false),
                Arguments.arguments(String.valueOf(curYear + 1), false),
                Arguments.arguments(String.valueOf(curYear + 10), false),
                Arguments.arguments("99999999999", false),
                Arguments.arguments("2022", false),
                Arguments.arguments("1970", true),
                Arguments.arguments("1999", true),
                Arguments.arguments("2000", true),
                Arguments.arguments("2010", true),
                Arguments.arguments(String.valueOf(curYear), true)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCheckHeight(String height, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkHeight(null, height));
    }

    static List<Arguments> testCheckHeight() {
        return List.of(
                Arguments.arguments("", false),
                Arguments.arguments("-10", false),
                Arguments.arguments("-1", false),
                Arguments.arguments("ABC120", false),
                Arguments.arguments("-@780", false),
                Arguments.arguments("123Q", false),
                Arguments.arguments("180.5", false),
                Arguments.arguments("0", false),
                Arguments.arguments("10", false),
                Arguments.arguments("30", true),
                Arguments.arguments("100", true),
                Arguments.arguments("175", true),
                Arguments.arguments("190", true),
                Arguments.arguments("220", true),
                Arguments.arguments("250", true),
                Arguments.arguments("251", false),
                Arguments.arguments("300", false),
                Arguments.arguments("999999999", false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testCheckWeight(String weight, boolean result) {
        Assertions.assertEquals(result, this.profileFormatChecker.checkWeight(null, weight));
    }
    static List<Arguments> testCheckWeight() {
        return List.of(
                Arguments.arguments("", false),
                Arguments.arguments("-1", false),
                Arguments.arguments("ABC120", false),
                Arguments.arguments("-@780", false),
                Arguments.arguments("123Q", false),
                Arguments.arguments("1", false),
                Arguments.arguments("9", false),
                Arguments.arguments("10", true),
                Arguments.arguments("20", true),
                Arguments.arguments("65", true),
                Arguments.arguments("300", true),
                Arguments.arguments("400", true),
                Arguments.arguments("401", false),
                Arguments.arguments("420", false),
                Arguments.arguments("9999999999", false)
        );
    }
}
