Feature: Mower management

  Scenario: Sample data input file
    Given Provide sample input file "classpath:input.data"
    When Run mower commands 
    Then I should be told the final mowers position is "1 3 N" and "5 1 E"
