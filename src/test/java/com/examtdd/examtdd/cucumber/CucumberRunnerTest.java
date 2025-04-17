package com.examtdd.examtdd.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "com.examtdd.examtdd.cucumber" }, plugin = {
    "pretty", "html:target/cucumber-reports" })
public class CucumberRunnerTest {
}