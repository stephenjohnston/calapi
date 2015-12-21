# calapi
Calendar/Holiday API library using java 8 streams.

This library has a few generic functions to generate a stream of dates based on rules.
The rules are:
 - Repeat a month/day every year
 - Repeat a month/day every year, and map the dates to alternate days depenending on a sub-rule.  For example:
    - Move Saturday to the previous Friday, or move Sunday to the following Monday
    - Move Sunday to Monday
 - Get the nth day-of-week for a month.  Repeate every year.
 - Get the nth to last day-of-week for a month.  Repeate every year.
 - Get repeating Easter dates
 - Get repeating Good Friday dates
 
 See the test suits for example usage.
