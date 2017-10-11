Feature: Mower management
  Based on an input file
  That contains mowers initial positions, orientation and command list
  I want to get the final position and orientation of mowers

  Scenario Outline: Sample data input file
    Given Provide sample input file "classpath:input.data"
    When Run mower commands 
    Then I should be told the final mowers position is "1 3 N\n5 1 E"