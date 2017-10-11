package fr.xebia.mower;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(
        features={"src/test/resources/feature/Mower.feature"}
)
public class RunMowerTest {

}
