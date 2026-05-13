Example output, shorted: 

Example output from sampleExecution():

1. Found customer: Customer{name='Sara Lind', phoneNumber='0701234567', ...}
2. Found bike: Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}
3-4. Created repair order: RepairOrder{orderId=1, state=NEWLY_CREATED, ...}
5. Repair tasks: Replace battery connector, Update motor controller firmware
7. Total cost: 1550
8. Repair order for approval: RepairOrder{orderId=1, state=READY_FOR_APPROVAL, ...}
9. Printer output:
Repair Order
Order ID: 1
State: ACCEPTED
Total Cost: 1550
10. Accepted repair order: RepairOrder{orderId=1, state=ACCEPTED, ...}


Example output, real output: 

1. Found customer: Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}
2. Found bike: Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}
3-4. Created repair order: RepairOrder{orderId=1, customer=Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}, bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}, problemDescription='Battery drains quickly and motor cuts out.', createdDate=2026-05-04, diagnosticReport=null, repairTasks=[], state=NEWLY_CREATED, estimatedCompletionDate=null}
5. Repair tasks: [RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}]
6-7. Updated repair order: RepairOrder{orderId=1, customer=Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}, bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}, problemDescription='Battery drains quickly and motor cuts out.', createdDate=2026-05-04, diagnosticReport=DiagnosticReport{reportText='Loose battery connector found and outdated firmware detected.'}, repairTasks=[RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}], state=NEWLY_CREATED, estimatedCompletionDate=2026-05-07}
7. Total cost: 1550
8. Repair order for approval: RepairOrder{orderId=1, customer=Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}, bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}, problemDescription='Battery drains quickly and motor cuts out.', createdDate=2026-05-04, diagnosticReport=DiagnosticReport{reportText='Loose battery connector found and outdated firmware detected.'}, repairTasks=[RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}], state=READY_FOR_APPROVAL, estimatedCompletionDate=2026-05-07}
9. Printer output:
Repair Order
Order ID: 1
State: ACCEPTED
Created Date: 2026-05-04
Estimated Completion Date: 2026-05-07
Customer: Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}
Bike: Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}
Problem Description: Battery drains quickly and motor cuts out.
Diagnostic Report: DiagnosticReport{reportText='Loose battery connector found and outdated firmware detected.'}
Repair Tasks: [RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}]
Total Cost: 1550

10. Accepted repair order: RepairOrder{orderId=1, customer=Customer{name='Sara Lind', phoneNumber='0701234567', email='sara.lind@example.com', bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}}, bike=Bike{brand='Crescent', model='Elina', serialNumber='SN-12345'}, problemDescription='Battery drains quickly and motor cuts out.', createdDate=2026-05-04, diagnosticReport=DiagnosticReport{reportText='Loose battery connector found and outdated firmware detected.'}, repairTasks=[RepairTask{description='Replace battery connector', cost=900}, RepairTask{description='Update motor controller firmware', cost=650}], state=ACCEPTED, estimatedCompletionDate=2026-05-07}
