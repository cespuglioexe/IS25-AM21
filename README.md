
<h4 align="center">Software Engineering Project for Politecnico di Milano 2024 - 2025</h4>
<p align="center">
    Amaducci Giacomo •
    Sergio Bolsieri •
    Thomas Bianconi •
    Stefano Carletto
</p>
<p align="center">
    Voto finale: 30/30
</p>

## Project Overview

This project is a Java-based implementation of the table game <a href="https://www.craniocreations.it/prodotto/galaxy-trucker">Galaxy Trucker</a>. 

### Implemented Features

| Functionality    | Status |
| :--------------- | :----: |
| Simplyfied Rules | ⚪️      |
| Complete Rules   | 🟢      |
| Socket Connection| 🟢      |
| RMI Connection   | 🟢      |
| CLI              | 🟢      |
| GUI              | 🟢      |
| Test Flight      | 🟢      |
| Multiple Matches | 🟢      |
| Percistance      | ⚪️      |
| Resilience       | ⚪️      |

🟢 = Implemented, ⚪️ = Not implemented

## How to use
#### Installation and execution
1. Download the `/out/artifacts/` directory, which contains `/Client_jar/Galaxy-Trucker.jar` and `/Server_jar/Galaxy-Trucker.jar`
2. To execute the two files, from the command line, navigate the the downloaded `/artifacts` folder and run `java -jar /Server_jar/Galaxy-Trucker.jar` to start the server application and `java -jar /Client_jar/Galaxy-Trucker.jar` to run the client application.
    **NB**: you can only have 1 instance of the server running at once, but any number of client instances.
   
#### Starting the server
Upon running with the earlier command, the server will prompt the user to enter the desired IP address.

- **Same-machine use**: Leaving the field empty will default to 'localhost', meaning the server will only allow connections from client applications running on the same machine.
- **Distributed use**: It is also possible to allow connections from clients running on other machines, but these **must be connected to the same local network**. When prompted for an IP address, insert your machines local IP address; this can be found in your devices internet settings:
  - Windows: from settings `Settings > Network & Internet > Details > IP address`, or from the command line `ipconfig` (IPv4)
  - MacOS: from settings `Settings > WiFi > Details > IP address`, or from the command line `ipconfig getifaddr en0`

#### Starting the client
When starting the client, you will be prompted to make two choices:

- **Connection technology (RMI or Socket)**: this has no impact on the function of the application, and an interface type
- **Interface type (GUI or CLI)**: GUI will launch a JavaFX application (after connection to the server) and CLI will remain in the command line

After choosing, you will be prompted to connect to the server:

- **Server IP adress**: if connecting to a server on the same machine, leave the field empty, otherwise insert the local IP address of the machine on which the server application is running
- **Server name / port number**: leave this field empty
