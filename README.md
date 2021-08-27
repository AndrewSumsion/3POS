# 3POS
An early development system to take restaurant orders from 3rd party ordering systems and insert them directly into a restaurant's point of sale system

## How it works

![design flow](https://github.com/AndrewSumsion/3POS/blob/master/doc/3POS-design-flow.png?raw=true)

This program is made up of three parts: the client, the client's plugins, and the server.

The client is a Java program that loads and manages the plugins and provides an API to send an order to the server.

The plugins contain code specifically for retrieving orders from the 3rd party systems.

The server is the program that directly interacts with the restaurant's Point of Sale system. The server is communicated with using an HTTP JSON API to make it so implementations can be written in any language to facilitate an easy integration with the Point of Sale program.

## What it is right now

**To see how the integration with Doordash/Uber Eats/etc. tablets works click [here](https://github.com/AndrewSumsion/FakeBluetoothPrinter).**

Right now I have created a simple working implementation of the design. This implementation works by simulating a bluetooth printer that ordering-service-issued tablets print receipts to. An employee prints a receipt of the order from the tablet, it goes to the simulated printer, an OCR program is run on it, the text is parsed into a JSON format, it is translated into a universal menu for the restaurant, then the order is inputted into the Point of Sale computer. This design uses several plugins: a plugin that simulates a bluetooth receipt printer and runs an OCR on the receipt, and a plugin for each ordering service that parses the OCR data into a universal JSON format that accounts for the differences between the different kinds of receipts. Currently the only ordering service I have implemented is Doordash.

![receipt printer design](https://github.com/AndrewSumsion/3POS/blob/master/doc/receipt-printer-design.png?raw=true)

## What is planned
I plan to make plugins for more 3rd party ordering systems (Uber Eats, Grubhub, etc.)

I plan to create server implementations for several popular Point of Sale systems
