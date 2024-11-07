# JFileShare
JhttpFileShare is a straightforward application designed for local data exchange. It provides a simple interface for sharing 
files across different devices within your network.

This application will start a local server to access your directories and transfer files to other devices.

## License
This project is licensed under the **Apache 2.0** license.<br>
**Copyright (c)** 2024 ShuraBlack<br>
For more information about the license, see [apache.org](https://www.apache.org/licenses/LICENSE-2.0)
or check the <b>LICENSE</b> file in the project

## Dependencies
❤️ Thanks to all the great programmers out there, which made all of this possible

This project requires Java 11+ [SDK](https://www.oracle.com/java/technologies/downloads/)<br>
All dependencies are managed by [Maven](https://maven.apache.org)<br>

Logging:
- [Log4j Core](https://github.com/apache/logging-log4j2) **2.20.0**
- [Log4j API](https://github.com/apache/logging-log4j2) **2.0.5**
- [Log4J Simple](https://github.com/apache/logging-log4j2) **2.0.5**

Data:
- [Json](https://github.com/stleary/JSON-java) **20240303**

Testing:
- [Junit5](https://junit.org/junit5/) **5.10.2**
- [Mockito](https://github.com/mockito/mockito) **5.11.0**

## Download ![Static Badge](https://img.shields.io/badge/version-v0.1.0-%230679b6)
This project only supports [GitHub Release](https://github.com/ShuraBlack/JFileShare/releases) with a **.jar** file.

## Java Arguments
```
JFileShare Server x.x.x

USAGE:
	java -jar JFileShare.jar [options/flags]

	-help						Shows this help
	-ip							Shows all Network Interfaces

FLAGS:
	-v, -verbose				Enables verbose mode (more informations Server-side)
	-n, -noroot					Disables the root directory restriction (Access entire file browser)
	-u, -upload					Enables uploading to the host mashine
	-h, -hidden					Also send informations about hidden files
	-ip=<network_name>			Sets the IP Address to the given network name [default: 0.0.0.0]
	-p, -port=<port>			Sets the Port [default: 80]
	-t, -threads=<size>			Sets the Thread Pool Size [default: 3]
	-r, -root=<path>			Sets the root folder [default: user.dir]
```

## Usage
Start the server with the following command:
```
java -jar JFileShare.jar 
```
The CLI will show you the IP Address and Port of the server. You can now access the server with any device in your local network.
> ❕Disclaimer: I recommend to keep the default port, since you dont need to write it explicitly in the url.

If you are not sure about the IP Address of your server, you can use the `-ip` flag to show all available network interfaces
and set the wanted Network Interface with the `-ip=<network_name>` option.
```
java -jar JFileShare.jar -ip=wlan0
```

## Example Page
![Example](https://github.com/user-attachments/assets/742815ed-1bb3-4717-aee6-423d6ace9d97)
