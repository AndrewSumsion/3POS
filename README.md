# 3POS
An early development system to take restaurant orders from 3rd party ordering systems and insert them directly into a restaurant's point of sale system

## How it works
This program is made up of three parts: the client, the client's plugins, and the server.

The client is a Java program that loads and manages the plugins and provides an API to send an order to the server.

The plugins contain code specifically for retrieving orders from the 3rd party systems.

The server is the program that directly interacts with the restaurant's Point of Sale system. The server is communicated with using an HTTP JSON API to make it so implementations can be written in any language to facilitate an easy integration with the Point of Sale program

## What it is right now
Right now the only part of the program that is done is the client as described above. This repository also contains an example plugin implementation which just takes JSON input.

## What is planned
I plan to implement plugins for every large 3rd party ordering system (Doordash, Uber Eats, Grubhub, etc.)

I plan to create server implementations for several popular Point of Sale systems

<sub><sup>Copyright Andrew Sumsion 2020, All Rights Reserved</sup></sub>
