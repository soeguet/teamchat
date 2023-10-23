# TeamChat
### v1.0.5 - reverted back to Gradle since 8.4 -- partial JDK-21 support was announced

<div style="text-align: center;">
<img src="pictures/img.png" alt="img" height="600"/>
</div>


NOTE: this is the reworked version.

TeamChat is a chat client built with Java for local network usage. This project is a hobby initiative, intended to
facilitate communication within a local network via WebSocket.

## Requirements

- Java 21
- Gradle 8.4
- [Websocket Backend](https://github.com/soeguet/teamsocket)

## Foundation

The core of TeamChat is based on TooTallNate's [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket) library,
and the GUI has been built using Java Swing.
The back-end connects to a PostgreSQL database.

## Important

Please note that TeamChat is currently only intended for use within a local network.
This is due to the absence of an additional encryption layer.

## Features

- Basic text chat.
- Emoji support.
- Quoting.
- Sending images, either through a file-picker or direct clipboard recognition.
- Setting nicknames and border colors.
- Desktop notifications. Either external (OS dependent) or internal (Java Swing JDialog).
- Conversation history, with the last 100 messages displayed on startup.
- Instant feedback on typing status.

### Planned Features

- Emoji reactions to messages.
- Ability to edit and delete your own messages.

## Usage

1. Clone the repository:

```bash
git clone https://github.com/soeguet/teamchat
```

2. Navigate to the folder and type

```bash
./gradlew build
``` 

in the terminal.

3a. Run the [Websocket Backend](https://github.com/soeguet/teamsocket)

3b. Run the application by typing

```bash
java -jar build/libs/teamchat.jar
```

## Environment Variables

Environment variables the chat client uses are listed below: (else it will prompt you to enter a username every time you
start the application)

- CHAT_USERNAME e.g. "John Doe"
- CHAT_IP e.g. "127.0.0.1"
- CHAT_PORT e.g. "8100"

## License

TeamChat is licensed under the [MIT License](https://opensource.org/license/mit/).

### Screenshots