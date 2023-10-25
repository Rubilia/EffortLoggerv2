# EffortLogger Prototype Integration

EffortLogger is a JavaFX-based prototype logging application. This README outlines the essential steps required to integrate a prototype into the EffortLogger application.

## Table of Contents
1. [Requirements](#requirements)
2. [Setup](#setup)
3. [Integration of a Prototype](#integration-of-a-prototype)
4. [Support & Feedback](#support--feedback)

## Requirements
- Java JDK 20
- JavaFX SDK

## Setup
1. Clone the repository to your local machine.
2. Ensure that JavaFX is set up correctly in your development environment.
3. Compile and run the application to ensure everything is set up correctly.

## Integration of a Prototype

To integrate a new prototype into the EffortLogger application, follow these steps:

1. Open the `EffortLoggerUI.java` file.
2. Navigate to the section where prototypes are added to the `riskReductionContainer`.
3. Modify the lines of code to specify information about your prototypes:

```java
// Edit these lines to specify information about your prototypes
riskReductionContainer.getChildren().addAll(
    createPrototypeBox("Concurrent Editing Prototype", "Concurrent editing allows multiple users to work simultaneously. EffortLogger ensures changes are accurate and avoids overwrites.", "Ilia Rubashkin"),
    createPrototypeBox("Prototype 2", "Description for Prototype 2", "Author2"),
    createPrototypeBox("Prototype 3", "Description for Prototype 3", "Author3"),
    createPrototypeBox("Prototype 4", "Description for Prototype 4", "Author4"),
    createPrototypeBox("Prototype 5", "Description for Prototype 5", "Author5")
);
