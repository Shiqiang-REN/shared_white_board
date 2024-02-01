# Distributed Shared WhiteBoard
## Overview
This project aims to create a distributed shared whiteboard that enables multiple users to collaborate in real-time. Users can draw on a shared canvas, supporting features like freehand drawing, shapes, text input, and color selection. The system is implemented in Java, and developers can choose between technologies like Sockets or Java RMI for building the distributed application.

## Main Challenges
Concurrency Handling: Addressing simultaneous actions to maintain a reasonable state.
System Structuring: Options include multiple servers communicating or a central server managing the system state.
Networked Communication: Deciding when and what messages are sent across the network, and designing an exchange protocol.
GUI Implementation: Utilizing tools like Java2D drawing package for functionality resembling MS Paint.
