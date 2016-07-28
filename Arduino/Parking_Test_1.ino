#include <Servo.h>
#include <Thread.h>
#include <ThreadController.h>
#include <SPI.h>
#include <WiFi.h>
//#include <pt.h>
char ssid[] = "LGArchi";              // The network SSID for ASUS secure network
//char pass[] = "16swarchitect";    // The network password for ASUS secure network
char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status

#define EntryBeamRcvr  34
#define ExitBeamRcvr   35

int EntryBeamState;
int ExitBeamState;

#define Slot1SensorPin 30
#define Slot2SensorPin 31
#define Slot3SensorPin 32
#define Slot4SensorPin 33

long  Slot1SensorVal;
long  Slot2SensorVal;
long  Slot3SensorVal;
long  Slot4SensorVal;

#define EntryGateGreenLED 26
#define EntryGateRedLED   27
#define ExitGateGreenLED  28
#define ExitGateRedLED    29
#define ParkingSlot1LED  22
#define ParkingSlot2LED  23
#define ParkingSlot3LED  24
#define ParkingSlot4LED  25


#define ParkingDetectBound 50
#define EntryGateServoPin 5
#define ExitGateServoPin 6
#define Open  90
#define Close 0

Servo EntryGateServo;
Servo ExitGateServo;

int delayvalue = 100;
boolean EntryGateOpenFlag = 1;
boolean EntryGateCloseFlag = 0;
boolean ExitGateOpenFlag = 1;
boolean ExitGateCloseFlag = 0;
boolean ParkingSlot1OccupiedFlag = 1;
boolean ParkingSlot2OccupiedFlag = 1;
boolean ParkingSlot3OccupiedFlag = 1;
boolean ParkingSlot4OccupiedFlag = 1;
boolean ParkingSlot1EmptyFlag = 0;
boolean ParkingSlot2EmptyFlag = 0;
boolean ParkingSlot3EmptyFlag = 0;
boolean ParkingSlot4EmptyFlag = 0;
boolean EmergencyFlag = 0;

#define SERVERPORTID  1005
#define RESERVINPUTPORTID 552
#define EXITOUTPUTPORTID 553
IPAddress server(192, 168, 1, 136); 
IPAddress reservinput(192, 168, 1, 219);
IPAddress exitoutput(192, 168, 1, 219);
WiFiClient client;
boolean ParkingStatus[4] = {false,false,false,false};
String ParkingID;
void setup() {
  InitEntryExitLEDs();   // You have to do this to turn off the
  while ( status != WL_CONNECTED)
  {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);

    status = WiFi.begin(ssid);
    Serial.println(status);
  }
  pinMode(EntryBeamRcvr, INPUT);     // Make entry IR rcvr an input
  digitalWrite(EntryBeamRcvr, HIGH); // enable the built-in pullup

  pinMode(ExitBeamRcvr, INPUT);      // Make exit IR rcvr an input
  digitalWrite(ExitBeamRcvr, HIGH);  // enable the built-in pullup

  pinMode(EntryGateGreenLED, OUTPUT);    // This section makes all the LED pins outputs.
  pinMode(EntryGateRedLED, OUTPUT);
  pinMode(ExitGateGreenLED, OUTPUT);
  pinMode(ExitGateRedLED, OUTPUT);
  pinMode(ParkingSlot1LED, OUTPUT);
  pinMode(ParkingSlot2LED, OUTPUT);
  pinMode(ParkingSlot3LED, OUTPUT);
  pinMode(ParkingSlot4LED, OUTPUT);

  digitalWrite(EntryGateGreenLED, HIGH);  // The gate LEDs are turned off by setting their pins
  digitalWrite(EntryGateRedLED, LOW);    // high. The reason for this is that they are
  digitalWrite(ExitGateGreenLED, HIGH);   // 3 color LEDs with a common annode (+). So setting
  digitalWrite(ExitGateRedLED, LOW);     // any of the other 3 legs low turns on the LED.

  EntryGateServo.attach(EntryGateServoPin);
  ExitGateServo.attach(ExitGateServoPin);
  EntryGateServo.write(Close);
  ExitGateServo.write(Close);
  digitalWrite(ParkingSlot1LED, LOW);    // Standard LEDs are used for the parking Slot
  digitalWrite(ParkingSlot2LED, LOW);    // LEDs. Set the pin high and they light.
  digitalWrite(ParkingSlot3LED, LOW);
  digitalWrite(ParkingSlot4LED, LOW);
  Serial.begin(9600);
}

void loop() {
  if(!EmergencyFlag){
  /**********************Gate Control*****************/
  /**Entry**/
  EntryBeamState = digitalRead(EntryBeamRcvr);
  ControlGate("Entry", EntryBeamState, EntryGateRedLED, EntryGateGreenLED, EntryGateServo, EntryGateOpenFlag, EntryGateCloseFlag, 
  "1"+GetParkingStatus(), "4", reservinput, RESERVINPUTPORTID, SendMsgOnEntryGate, ReportServer);
  /**Exit**/
  ExitBeamState = digitalRead(ExitBeamRcvr);
  ControlGate("Exit", ExitBeamState, ExitGateRedLED, ExitGateGreenLED, ExitGateServo, ExitGateOpenFlag, ExitGateCloseFlag, 
  "5","6", exitoutput, EXITOUTPUTPORTID, SendMsgOnExitGate, ReportServer);
  
  /***************************************************/
  /************************Parking Slot Control*******/
  Slot1SensorVal = ProximityVal(Slot1SensorPin);
  DetectOccupancyForSlot(0, "Slot1", Slot1SensorVal, ParkingSlot1OccupiedFlag, ParkingSlot1EmptyFlag);

  Slot2SensorVal = ProximityVal(Slot2SensorPin);
  DetectOccupancyForSlot(1, "Slot2",Slot2SensorVal, ParkingSlot2OccupiedFlag, ParkingSlot2EmptyFlag);

  Slot3SensorVal = ProximityVal(Slot3SensorPin);
  DetectOccupancyForSlot(2, "Slot3",Slot3SensorVal, ParkingSlot3OccupiedFlag, ParkingSlot3EmptyFlag);

  Slot4SensorVal = ProximityVal(Slot4SensorPin);
  DetectOccupancyForSlot(3, "Slot4",Slot4SensorVal, ParkingSlot4OccupiedFlag, ParkingSlot4EmptyFlag);
  
  //ReportServer("7"+GetParkingStatus(), server, SERVERPORTID);
  }
  
  /***************************************************/
  /************************Forced Gate Contol*********/
  int data = Serial.read();
  if(data == '1'){
    if(EmergencyFlag){
      EntryGateServo.write(Open);
    }
  }
  if(data == '2'){
    if(EmergencyFlag){
      EntryGateServo.write(Close);
    }
  }  
  if(data == '3'){
    if(EmergencyFlag){
      ExitGateServo.write(Open);
    }
  }
  if(data == '4'){
    if(EmergencyFlag){
      ExitGateServo.write(Close);
    }
  }
  if(data == '5'){
    EmergencyFlag = true;
  }
  if(data == '6'){
    EmergencyFlag = false;
  }
  /***************************************************/
  //delay(delayvalue);
}
void ControlGate(String GateName, int BeamState, int RedLedPin, int GreenLedPin, Servo GateServo, boolean& OpenFlag, boolean& CloseFlag, 
String message1, String message2, IPAddress serverip, int portnumber,int (*ServerFunction1)(String, IPAddress, int),int (*ServerFunction2)(String, IPAddress, int) ){
  if (BeamState == LOW) {
    if (OpenFlag) {
      delay(250);
      digitalWrite(GreenLedPin, LOW);
      if(ServerFunction1(message1, serverip, portnumber)){
        Serial.println(GateName+" GateOpen");
        digitalWrite(RedLedPin, HIGH);
        digitalWrite(GreenLedPin, LOW);
        GateServo.write(Open);
        OpenFlag = 0;
        CloseFlag = 1;
      }else{
        digitalWrite(GreenLedPin, HIGH);
        for(int i = 0 ; i < 3 ; i++){
          delay(250);
          digitalWrite(RedLedPin, LOW);
          delay(250);
          digitalWrite(RedLedPin, HIGH);
        }
        digitalWrite(RedLedPin, LOW);
        OpenFlag = 1;
        CloseFlag = 0;
      }
    }
  } else {
    if (CloseFlag) {
      digitalWrite(GreenLedPin, LOW);
      ServerFunction2(message2, serverip, portnumber);
      Serial.println(GateName+" GateClose");
      digitalWrite(RedLedPin, LOW);
      digitalWrite(GreenLedPin, HIGH);
      GateServo.write(Close);
      OpenFlag = 1;
      CloseFlag = 0;
     // }
    }
  }
}
void DetectOccupancyForSlot(int SlotNumber, String SlotName, long SlotVal,  boolean& OccupiedFlag, boolean& EmptyFlag) {
  if (SlotVal < ParkingDetectBound) {
    if (OccupiedFlag) {
      ParkingStatus[SlotNumber] = true;
      ReportServer("2"+SlotName+GetParkingStatus()+" "+ParkingID, server, SERVERPORTID);
      Serial.println("Occupied"+SlotNumber);
      digitalWrite(ParkingSlot1LED, LOW);    // Standard LEDs are used for the parking Slot
      digitalWrite(ParkingSlot2LED, LOW);    // LEDs. Set the pin high and they light.
      digitalWrite(ParkingSlot3LED, LOW);
      digitalWrite(ParkingSlot4LED, LOW);
      OccupiedFlag = 0;
      EmptyFlag = 1;
    }
  } else {
    if (EmptyFlag) {
      ParkingStatus[SlotNumber] = false;
      ReportServer("3"+SlotName+GetParkingStatus(), server, SERVERPORTID);
      ReportServer("3"+SlotName+GetParkingStatus(), exitoutput, EXITOUTPUTPORTID);
      Serial.println("Empty"+SlotNumber);
      OccupiedFlag = 1;
      EmptyFlag = 0;
    }
  }
}

int ReportServer(String message, IPAddress address, int port){
  if(message=="NULL")
    return -1;
  if (client.connect(address, port)) {
    Serial.println("Connected with the server");
    Serial.println(message);
    client.println(message);
    client.println("END");  
     Serial.print("Server Message: ");
    char c = ' ';
    String str;    
    while ( c != '\n' )
    {
      if (client.available())
      {
        c = client.read();
        if( c != '\n')
        {
           str+=c;
        }
      }
    }
    client.flush();
    client.stop();
     Serial.println(str);
    return 0;
  }
}
int SendMsgOnEntryGate(String message, IPAddress address, int port) {
  //TODO::Make String to return server message
  if(message=="NULL")
    return -1;
  if (client.connect(address, port)) {
    Serial.println("Connected with the server");
    Serial.println(message);
    client.println(message);
    client.println("END");
    Serial.print("Server Message: ");
    char c = ' ';
    String str;    
    while ( c != '\n' )
    {
      if (client.available())
      {
        c = client.read();
        if( c != '\n')
        {
           str+=c;
        }
      }
      if(digitalRead(EntryBeamRcvr) == HIGH ){
        Serial.println("break!");
        client.stop();
        return 0;
      }
      
    }
    client.flush();
    client.stop();
    Serial.println(str);
    if(str.substring(0,5) =="1Auth"){
      ParkingID = str.substring(7, str.length());
      Serial.println(str.substring(7, str.length()));
      Serial.println("TRUE");
      Serial.println((str.charAt(5)-48)+ParkingSlot1LED );
      digitalWrite((str.charAt(5)-48)+ParkingSlot1LED , HIGH);
      return 1;
    }else{
      Serial.println("FALSE");
      return 0;
    }
  }
}
int SendMsgOnExitGate(String message, IPAddress address, int port){
  if(message=="NULL")
    return -1;
  if (client.connect(address, port)) {
    Serial.println("Connected with the server");
    Serial.println(message);
    client.println(message);
    client.println("END");
    Serial.print("Server Message: ");
    char c = ' ';
    String str;    
    while ( c != '\n' )
    {
      if (client.available())
      {
        c = client.read();
        if( c != '\n')
        {
           str+=c;
        }
      }
      if(digitalRead(ExitBeamRcvr) == HIGH ){
        Serial.println("break!");
        client.stop();
        return 0;
      }
      
    }
    client.flush();
    client.stop();
    Serial.println(str);
    if(str.substring(0,5) =="5Succ"){
      Serial.println("TRUE");
      return 1;
    }else{
      Serial.println("FALSE");
      return 0;
    }
  }
}

 
String GetParkingStatus(){
  String temp =" ";
  for (int i = 0 ; i < sizeof(ParkingStatus) ; i++){
    temp += ParkingStatus[i];
  }
  return temp;
}

long ProximityVal(int Pin)
{
  long duration = 0;
  pinMode(Pin, OUTPUT);         
  digitalWrite(Pin, HIGH);      
  delay(1);                    

  pinMode(Pin, INPUT);         
  digitalWrite(Pin, LOW);       
  while (digitalRead(Pin))     
  { // LOW (cap discharges)
    duration++;
  }
  return duration;              
}

void InitEntryExitLEDs()
{
  int i;
  for (i = 26; i <= 29; i++)
  {
    pinMode(i, OUTPUT);
    digitalWrite(i, HIGH);
  }
}
