Repair Electric Bike, Seminar 4
================================

This implementation extends the seminar 3 program with the seminar 4 requirements.

Requirements:

* Java 17 or newer.
* Maven 3.9 or newer.

Continuous integration:

* GitHub Actions runs `mvn -q test` on push and pull request.
* Maven Wrapper is not committed. Generate it with `mvn -N wrapper:wrapper` if the submission environment requires `./mvnw`.

Run the program:

```sh
cd "Sem 4"
mvn clean compile exec:java
```

Run the tests:

```sh
cd "Sem 4"
mvn test
```

Seminar report:

* [docs/report.md](docs/report.md)

Runtime log files are created in `Sem 4/` when the sample program is executed:

* `repair-error-log.txt` contains developer error reports for technical failures.
* `repair-order-log.txt` contains observer updates for repair orders.

Implemented seminar 4 requirements:

* Checked exceptions are used for customer search errors.
* `CustomerNotFoundException` is shown to the user when no customer exists for the searched phone number.
* `OperationFailedException` wraps simulated database failures before the exception reaches the view.
* The hard-coded phone number `9999999999` simulates a customer database failure.
* User-facing error messages are printed by `ErrorMessageHandler`.
* Developer error reports are written to `repair-error-log.txt`.
* `RepairOrder` is the observed object in the Observer pattern.
* `RepairOrderObserver` is implemented by `RepairOrderView` and `RepairOrderLogger`.
* Repair order updates are printed to `System.out` and written to `repair-order-log.txt`.
* Singleton is used for `CustomerRegistry`.
* Strategy is used for repair order discounts. `Main` uses `WarrantyDiscountStrategy`, while tests also cover `NoDiscountStrategy`.
* Bike warranty is explicit domain data, not inferred from serial number format.
* Invalid repair order state transitions are reported with `InvalidRepairOrderStateException`.
* Repair order logs mask customer name, phone number, and email.
* Amounts are kept as `BigDecimal` in repair order DTOs and formatted without converting totals to `double`.

Example output from `View.sampleExecution()`:

```text
ERROR: No customer was found with phone number 0000000000.
0a. Missing customer search result: none
ERROR: The customer search could not be completed. Please try again later.
0b. Database failure search result: none
1. Found customer: Customer{name='Sara Lind', phoneNumber='0701234567', ...}
2. Found bike: Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}
RepairOrderView update: RepairOrder{orderId=1, state=NEWLY_CREATED, totalCost=0, ...}
3-4. Created repair order: RepairOrder{orderId=1, state=NEWLY_CREATED, totalCost=0, ...}
5. Repair tasks: [RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}]
RepairOrderView update: RepairOrder{orderId=1, state=NEWLY_CREATED, totalCost=1395, ...}
6-7. Updated repair order: RepairOrder{orderId=1, state=NEWLY_CREATED, totalCost=1395, ...}
7. Total cost: 1395
RepairOrderView update: RepairOrder{orderId=1, state=READY_FOR_APPROVAL, totalCost=1395, ...}
8. Repair order for approval: RepairOrder{orderId=1, state=READY_FOR_APPROVAL, totalCost=1395, ...}
9. Printer output:
RepairOrderView update: RepairOrder{orderId=1, state=ACCEPTED, totalCost=1395, ...}
Repair Order
Order ID: 1
State: ACCEPTED
Total Cost: 1395
10. Accepted repair order: RepairOrder{orderId=1, state=ACCEPTED, totalCost=1395, ...}
```
