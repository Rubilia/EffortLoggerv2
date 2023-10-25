# EffortLogger Prototype Integration

EffortLogger is a JavaFX-based prototype logging application. This README outlines the essential steps required to integrate a prototype into the EffortLogger application.

## Table of Contents
1. [Requirements](#requirements)
2. [Setup](#setup)
3. [Integration of a Prototype](#integration-of-a-prototype)
4. [Installing JSON Package for Concurrent Editing Prototype](#installing-json-package-for-concurrent-editing-prototype)

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
3. Modify the lines of code to specify information about your prototypes and associated actions:

```java
// Edit these lines to specify information about your prototypes
riskReductionContainer.getChildren().addAll(
    createPrototypeBox("Concurrent Editing Prototype", "Concurrent editing allows multiple users to work simultaneously. EffortLogger ensures changes are accurate and avoids overwrites.", "Ilia Rubashkin", StartConcurrentEditingPrototype()),
    createPrototypeBox("Prototype 2", "Description for Prototype 2", "Author2", StartPrototypeTwo()),
    createPrototypeBox("Prototype 3", "Description for Prototype 3", "Author3", StartPrototypeThree()),
    createPrototypeBox("Prototype 4", "Description for Prototype 4", "Author4", StartPrototypeFour()),
    createPrototypeBox("Prototype 5", "Description for Prototype 5", "Author5", StartPrototypeFive())
);
```

To start your own prototype, you should write the code inside the corresponding function:

```java
private Runnable StartPrototypeTwo() {
    return () -> {
        // Start your prototype
    };
}

private Runnable StartPrototypeThree() {
    return () -> {
        // Start your prototype
    };
}

private Runnable StartPrototypeFour() {
    return () -> {
        // Start your prototype
    };
}

private Runnable StartPrototypeFive() {
    return () -> {
        // Start your prototype
    };
}
```


## Installing JSON Package for Concurrent Editing Prototype

Download the ZIP file from this [URL](http://www.java2s.com/Code/Jar/j/Downloadjavajsonjar.htm) and extract it to get the Jar. Add the Jar to your build path.

To add this Jar to your build path:
- Right-click the Project
- Navigate to Build Path > Configure build path
- Select the Libraries tab
- Click Add External Libraries
- Select the downloaded Jar file

