package com.eckrin.test.spring_batch.validateparam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

@Slf4j
public class FileUtils {

    public static void validateCsv(String fileName) throws JobParametersInvalidException {

        if(!StringUtils.endsWithIgnoreCase(fileName, "csv")) {
            throw new JobParametersInvalidException("This is not csv file.");
        }
    }
}
