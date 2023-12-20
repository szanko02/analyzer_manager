#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

/* Установите здесь свои SSID и пароль */
const char* ssid = "NodeMCU";       // SSID
const char* password = "12345678";  // пароль

/* Настройки IP адреса */
IPAddress local_ip(192,168,169,169);
IPAddress gateway(192,168,169,169);
IPAddress subnet(255,255,255,0);

ESP8266WebServer server(80);
String strs[4];
void setup() 
{
  Serial.begin(115200);

  WiFi.softAP(ssid, password);
  WiFi.softAPConfig(local_ip, gateway, subnet);
  delay(100);

  server.on("/", handle_OnConnect);
  server.onNotFound(handle_NotFound);

  server.begin();
  pinMode(2, OUTPUT);
  delay(1000);
  digitalWrite(2, HIGH);
  delay(1000);
  digitalWrite(2, LOW);
  delay(10);
  //Serial.println("");
  //Serial.println("HTTP server started");
}

void loop() 
{
  server.handleClient();
}

void handle_OnConnect() 
{ 
  int length = 0;
  String data = "";
  byte buferByte1 = 0x00;
  byte buferByte2 = 0x00;
  byte buferByte3 = 0x00;
  if (server.hasArg("data")){
    data = server.arg("data");
    length = Spliter(strs, data, "_");
    if (strs[0] == "brightness"){
      Serial.write(0x00);
      Serial.write((byte)strs[1].toInt());
    } else if (strs[0] == "color1"){
      Serial.write(0x01);
      Serial.write((byte)strs[1].toInt());
      Serial.write((byte)strs[2].toInt());
      Serial.write((byte)strs[3].toInt());
    } else if (strs[0] == "color2"){
      Serial.write(0x02);
      Serial.write((byte)strs[1].toInt());
      Serial.write((byte)strs[2].toInt());
      Serial.write((byte)strs[3].toInt());
    } else if (strs[0] == "color3"){
      Serial.write(0x03);
      Serial.write((byte)strs[1].toInt());
      Serial.write((byte)strs[2].toInt());
      Serial.write((byte)strs[3].toInt());
    } else if (strs[0] == "color4"){
      Serial.write(0x04);
      Serial.write((byte)strs[1].toInt());
      Serial.write((byte)strs[2].toInt());
      Serial.write((byte)strs[3].toInt());
    } else if (strs[0] == "width"){
      Serial.write(0x05);
      Serial.write((byte)strs[1].toInt());
    }else if (strs[0] == "height"){
      Serial.write(0x06);
      Serial.write((byte)strs[1].toInt());
    } else if (strs[0] == "smooth"){
      Serial.write(0x07);
      Serial.write((byte)strs[1].toInt());
    }
    server.send(200, "text/plain", server.arg("data")+"+"+server.arg("data")); }
  else{
    //Serial.println("OnConnect_NODATA");
    server.send(200, "text/plain","OnConnectNO_DATA");
  }
}

void handle_NotFound()
{
  Serial.println("NotFound");
  server.send(404, "text/plain", "Not found");
}

int Spliter(String* strs, String input, String delimeter) {
  int StringCount = 0;
  while (input.length() > 0) {
    int index = input.indexOf(delimeter);
    if (index == -1) {
      strs[StringCount++] = input;
      break;
    } else {
      strs[StringCount++] = input.substring(0, index);
      input = input.substring(index + 1);
    }
  }
  return StringCount;
}
