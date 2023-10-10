
# Online whiteboard

## Introduction
This project involves the implementation of a shared whiteboard that allows multiple users to draw on the canvas simultaneously. The core concept of this project revolves around Remote Method Invocation (RMI), where the server and clients communicate through Java RMI. The server initializes whiteboards and permits multiple users to join these online whiteboards. The first user to join becomes the manager, granting them additional privileges, while subsequent users are categorized as normal users. The implementation utilizes ConcurrentHashMap to manage client interactions, ensuring thread-safe operations and consistency during concurrent access.

## System Architecture
The project follows a client-server architectural model with one server and multiple clients. The server provides requested services on the whiteboard, while clients request services.

## Communication Protocols and Message Formats
Remote Method Invocation (RMI): RMI is employed for communication between whiteboard components.
IRemoteWhiteBoard: Implements RMI and allows clients to access whiteboard information.
IRemoteServer: Provides communication methods between the server and clients.
IRemoteClient: Allows clients to connect to the server, update the user interface, and exchange whiteboard information.

## Design Diagrams
UML Diagram: Provides an overview of the entire project.
Sequence Diagram: Illustrates the interactions within the whiteboard.

## Implementation Details
The project is organized into four packages:

Remote Interface Package:

Defines interfaces (IRemoteClient, IRemoteServer, IRemoteWhiteboard) for client, server, and whiteboard interactions.
Icon Package:

Contains necessary icon images used in the whiteboard.
Server Package:

RemoteBoardServant: Registers new users, manages clients, broadcasts whiteboard information.
ClientManager: Manages users, allowing addition, deletion, and iteration through clients.
BoardServer: Uses Java RMI to initialize the whiteboard and handle client access.
Client Package:

Client: Displays the client's user interface, including the whiteboard canvas, color options, shape buttons, chat box, and user list. Manages client actions and interactions.
AdvancedFeature: Implements advanced functions like file open and save.
Color_Button: Creates buttons with specified background colors.
IconAddress: Defines paths for icons used in the user interface.
ModeShape: Implements various drawing shapes.
ToolButton: Creates buttons with icons and click event listeners.
Message: Implements RMI interfaces for passing whiteboard information.
Whiteboard: Initializes the whiteboard area in grid form, captures actions, and communicates whiteboard messages to other users.

## New Innovations
Additional Drawing Shapes: Implemented triangle and trapezoid shapes for enhanced drawing options.
Grid Layout: The whiteboard is organized in a grid layout, aiding in aligning and placing graphical elements accurately.
Manager Avatar: Added an administrator avatar to the manager's whiteboard for role differentiation.





