# Repair Electric Bike Seminar 4 Report

## Method

The implementation was developed from the seminar 3 code and the seminar 4 requirements. The first step was to identify the error situations that are actual exceptions in the use case: a customer search for a phone number that does not exist, and a simulated database failure. Both are represented by checked exceptions because callers are expected to handle them and continue the program flow.

The integration layer throws exceptions that match integration-level problems: `NoSuchCustomerException` and `DatabaseFailureException`. The controller catches these and throws controller-level exceptions: `CustomerNotFoundException` and `OperationFailedException`. This keeps the abstraction level correct, since the view does not need to know how the customer registry is implemented.

The Observer pattern was implemented with `RepairOrder` as the observed object. This is the object whose state changes when diagnostic reports are added, orders are prepared for approval, and orders are accepted or rejected. Observers implement `RepairOrderObserver` and receive a `RepairOrderSnapshot`, which is immutable data copied from the order. This avoids exposing the mutable repair order object to views and loggers.

Two additional GoF patterns were implemented. `CustomerRegistry` is a Singleton because the in-memory customer registry represents one shared external data source. Discount calculation uses Strategy: `RepairOrder` delegates discount calculation to a `DiscountStrategy`, and `Main` uses `WarrantyDiscountStrategy`. Warranty is represented explicitly by `Bike.warrantyEndDate`, instead of being inferred from a serial number format.

Unit tests were added or updated while implementing each change. Exception tests cover both thrown exceptions and the view catch blocks. Loggers can now receive a `PrintWriter`, which makes logging behavior testable without writing test data to real log files.

Invalid repair order state transitions are treated as errors at the controller level. The model returns whether a state-changing operation succeeded, and the controller maps a failed transition to `InvalidRepairOrderStateException`. This makes failed use-case steps explicit to callers instead of silently returning unchanged data.

The first observer notification is published after the new repair order has been registered. This avoids external side effects in the `RepairOrder` constructor and makes the object lifecycle clearer.

Money is represented by `Amount`, which stores `BigDecimal`. Repair order DTOs now keep totals as `BigDecimal`, and formatting uses decimal string output instead of converting totals through `double`.

Repair order log entries are formatted separately from user-facing output. The log formatter masks customer name, phone number and email to avoid writing personal data in clear text.

## Result

The application still follows the layered structure from the earlier seminars:

* `startup` creates and wires dependencies.
* `view` contains console output, error messages, and log output.
* `controller` coordinates calls between view, model, and integration.
* `integration` simulates external registries and printer behavior.
* `model` contains the domain objects, state changes, observers, and discount strategies.
* `dto` contains data transfer objects used between controller and view.

Important changed classes:

* `CustomerRegistry` throws checked exceptions for missing customers and simulated database failure.
* `Controller` maps lower-level exceptions to controller-level exceptions and no longer returns `null` for missing selected customer or missing repair order.
* `Controller` throws `InvalidRepairOrderStateException` when a repair order operation is not allowed in the current state.
* `RepairOrder` owns the observer list and notifies observers when its state or contents change.
* `RepairOrderView` prints repair order snapshots to `System.out`.
* `RepairOrderLogger` writes masked repair order snapshots to `repair-order-log.txt`.
* `ErrorLogger` writes developer error summaries to `repair-error-log.txt`.
* `Bike` now contains warranty data used by `WarrantyDiscountStrategy`.

Sample run:

```text
ERROR: No customer was found with phone number 0000000000.
ERROR: The customer search could not be completed. Please try again later.
RepairOrderView update: RepairOrder{orderId=1, state=NEWLY_CREATED, ...}
RepairOrderView update: RepairOrder{orderId=1, state=READY_FOR_APPROVAL, ...}
RepairOrderView update: RepairOrder{orderId=1, state=ACCEPTED, ...}
Repair Order
Order ID: 1
State: ACCEPTED
Total Cost: 1395
```

The project can be built and tested with Maven:

```sh
cd "Sem 4"
mvn test
mvn exec:java
```

## Discussion

The exception handling follows the seminar criteria by using checked exceptions for expected recoverable errors, naming exception classes after the error condition, including the searched phone number or order id, preserving state when exceptions are thrown, showing user-friendly messages, and logging technical failures for developers.

The Observer implementation is now placed on the observed domain object instead of in the controller. This improves cohesion because `RepairOrder` owns its own state changes and event notification. It also lowers coupling from the controller to observer behavior: the controller only registers observers when creating a repair order.

The data passed to observers is a `RepairOrderSnapshot`, not the mutable `RepairOrder`. This protects encapsulation and gives observers all data needed to print or log the update.

The Singleton use is intentionally small and limited to the simulated customer registry. In a larger application a dependency injection container would be preferable, but for this assignment the Singleton demonstrates the pattern without spreading static access through the code.

The Strategy implementation is used for discount calculation because discount rules can vary without changing `RepairOrder`. Explicit warranty data in `Bike` makes the strategy more domain-oriented than checking a hard-coded serial number prefix.

The solution intentionally uses simple file logging instead of a logging framework, since external frameworks are outside the seminar scope. The log entries are nevertheless separated from user-facing formatting and avoid clear-text personal data.

Remaining limitations are mostly due to the assignment scope. The registries are still in-memory simulations, and the view is a hard-coded sample flow rather than an interactive UI. Maven Wrapper is not committed because it must be generated with Maven in an environment that can download the wrapper files. This is acceptable for the seminar requirements, where the focus is exception handling, observer behavior, design patterns, and tests.
