# Subscribe and Save Revisited: Unit Testing updateSubscription

## Background

This Subscribe and Save MLP (Minimum Loveable Product) provides the functionality to create subscriptions and retrieve 
them. Some of you may have seen it in our Debugging lesson a couple of weeks ago.

If we haven't already, let's take a look at the class diagram and discuss it as a class.
If you already installed the `PlantUML integration` plugin, you can open Activity_CD.plantuml in IntelliJ.
***NOTE:*** Light grey classes are classes we will assume to be tested and functional today.
Feel free to step through them and take a look while debugging, but we can assume our bugs aren't
hiding in there. 

Now that we've got the basics of creating JUnit tests, we've rewritten some of our unit tests from before
as proper JUnit tests (rather than methods we call from a `main()` method--which works ok, but we've found
it way more convenient to use a framework like JUnit for several reasons, including being able to run
individual tests within IntelliJ, and because we can run the tests and get individual test failure reports
when we build the package).

Around the same time, due to lots of +1s on the SIM for an update API, one of our colleagues has started
to implement the promised "update" API that allows updating an existing subscription.
The primary use case is updating the frequency on an existing subscription, but the back end will allow
changing the customer or ASIN as well.

This development is only just getting started, so a first cut at the implementation has been added to the
`SubscriptionFileStorage` class, but so far nothing is calling it. We've managed to add a single JUnit test
for the `updateSubscription` method:
`updateSubscription_withExistingSubscriptionAndUpdatedFrequency_returnsUpdatedFrequency`. This test
updates the frequency of an existing subscription, and verifies that the returned `Subscription` object
indeed has the frequency updated.

Your job is to add more tests to cover the use cases.

## Review the code

Look at the javadoc for the `updateSubscription` method in
`com.amazon.ata.unittesting.subscribeandsave.dao.SubscriptionFileStorage` (HINT: look under src/ in
a package close to the one containing this README file

Look at the existing test in `SubscriptionFileStorageTest` (same package as `SubscriptionFileTest`, but under
the tst/ directory). Ask any questions you have about this test.

## What are the relevant test cases?

Think about the top few test cases that this new method needs. Use the javadoc you read above, plus
the existing test cases in
`com.amazon.ata.unittesting.subscribeandsave.dao.SubscriptionFileStorageTest`
(look under the tst/ directory).

Be prepared to share some of your test cases in class discussion.

## Write your tests (and see if you can spot a bug!)

1. Write 2 new unit tests for the `updateSubscription` method. See if any of those tests fail and reveal
   a true bug. We hear reports that the frequency field doesn't seem to be getting updated when we update a
   subscription and then try to get it again....Be sure to include this test case in your 2!
1. See the Appendix at the bottom of this file for where to look for data!
1. Add your tests to the bottom of the `SubscriptionFileStorageTest` class (there's a placeholder there for you)
1. When you've written your new JUnit tests, commit and push your code.
    * NOTE: The test case above should be failing!
1. Add a code.amazon.com link to this commit to this activity's Canvas Quiz.

## Extension: If you spot a bug, fix the code!

1. See if you can fix the code and get your failing test(s) to pass.
1. If you fix the bug, commit and push your code.
1. Add a code.amazon.com link to this commit to this activity's Canvas Quiz.

## Super-Extension: Integrate the SubscriptionFileStorage into SNS

1. Go ahead and see if you can use the new `updateSubscription` method in the `SubscriptionDAO` and
   `SubscriptionService` classes to implement the update Subscription API.
1. Create a unit test for SubscriptionService to verify that subscriptions can be successfully updated
   (the same scenario that had the bug above). Add to the existing `SubscriptionServiceTest` test class
   in the tst/ directory.
1. If you make any progress on this, commit and push your code.
1. Add a code.amazon.com link to this commit to this activity's Canvas Quiz.

## Appendix: DATA

If you look at the top directory of this package, there's a directory called "resources". If you look inside
there, you'll see `resources/unittesting/classroom/subscribeandsave/` and several text files inside it:
* `catalog.json`: lists the products currently available for testing in the catalog (use this to find valid ASINs)
* `customers.txt`: a list of valid test customer IDs
* `subscriptions.csv.restore`: A list of existing test subscriptions that will be available every time you run
  your tests.

If you really want, you can add records to these if it helps you. We suggest that you do not modify or remove
of the data in these files, as this might cause tests to start failing!
