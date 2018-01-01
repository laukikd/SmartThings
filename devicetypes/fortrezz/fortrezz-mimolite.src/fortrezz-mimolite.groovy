/**
 *  FortrezZ MIMOlite generic IO Module
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Based on Todd Wackford's MimoLite Garage Door Opener and FortrezZ Flow Meter Interface
 */
metadata {
	// Automatically generated. Make future change here.
	definition (name: "FortrezZ MIMOlite Generic", namespace: "fortrezz", author: "laukikd") {
		capability "Configuration"
		capability "Switch"
		capability "Refresh"
		capability "Contact Sensor"
        capability "Voltage Measurement"

		attribute "powered", "string"

		command "on"
		command "off"
        
        fingerprint deviceId: "0x1000", inClusters: "0x72,0x86,0x71,0x30,0x31,0x35,0x70,0x85,0x25,0x03"
	}

	simulator {
	// Simulator stuff
    
	}
    
    preferences {
       //input "RelaySwitchDelay", "decimal", title: "Delay between relay switch on and off in seconds. Only Numbers 0 to 3.0 allowed. 0 value will remove delay and allow relay to function as a standard switch", description: "Numbers 0 to 3.1 allowed.", defaultValue: 0, required: false, displayDuringSetup: true
       
       input "Group1", "decimal", title: "Basic set command on Trigger/UnTrigger True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 0, required: false, displayDuringSetup: true
       input "Group2", "decimal", title: "Periodic multiLevel sensor report based on Param9 True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 0, required: false, displayDuringSetup: true
       input "Group3", "decimal", title: "Alarm command class report for power droupout True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 1, required: false, displayDuringSetup: true
       input "Group4", "decimal", title: "Binary sensor command on Trigger/UnTrigger True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 1, required: false, displayDuringSetup: true
       input "Group5", "decimal", title: "Pulse meter count based on Param9 True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 0, required: false, displayDuringSetup: true
       
       input "Param2", "decimal", title: "Reset Pulse Count True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 1, required: false, displayDuringSetup: true
       input "Param3", "decimal", title: "Relay Triggered by SIG1 True(1)/False(0)", description: " True(1)/False(0)", defaultValue: 0, required: false, displayDuringSetup: true
       input "Param4", "decimal", title: "LowerThreshold High", description: "Numbers 0 to 255 allowed.", defaultValue: 210, required: false, displayDuringSetup: true
       input "Param5", "decimal", title: "LowerThreshold Low", description: "Numbers 0 to 255 allowed.", defaultValue: 198, required: false, displayDuringSetup: true
       input "Param6", "decimal", title: "HigherThreshold High", description: "Numbers 0 to 255 allowed.", defaultValue: 255, required: false, displayDuringSetup: true
       input "Param7", "decimal", title: "HigherThreshold Low", description: "Numbers 0 to 255 allowed.", defaultValue: 254, required: false, displayDuringSetup: true
       input "Param8_1", "decimal", title: "Analog(0)/Digital(1) Trigger", description: "Analog(0)/Digital(1)", defaultValue: 0, required: false, displayDuringSetup: true
       input "Param8_0", "decimal", title: "Trigger Outside(0)/Between(1) Threshold", description: "Between(1)/Outside(0)", defaultValue: 1, required: false, displayDuringSetup: true
       input "Param9", "decimal", title: "Periodic Reporting Interval. (Value x 10s)", description: "Numbers 0 to 255 allowed.", defaultValue: 30, required: false, displayDuringSetup: true
       input "Param11", "decimal", title: "Momentary Relay Enable (Value x 100ms)", description: " Numbers 0 to 255 allowed.", defaultValue: 0, required: false, displayDuringSetup: true
    }


	// UI tile definitions 
	tiles (scale: 2) {
        standardTile("switch", "device.switch", width: 4, height: 4, canChangeIcon: false, decoration: "flat") {
            state "on", label: "On", action: "off", icon: "http://swiftlet.technology/wp-content/uploads/2016/06/Switch-On-104-edit.png", backgroundColor: "#53a7c0"
			state "off", label: 'Off', action: "on", icon: "http://swiftlet.technology/wp-content/uploads/2016/06/Switch-Off-104-edit.png", backgroundColor: "#ffffff"
        }
        standardTile("contact", "device.contact", width: 2, height: 2, inactiveLabel: false) {
			state "open", label: '${name}', icon: "st.contact.contact.open", backgroundColor: "#ffa81e"
			state "closed", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#79b821"
		}
        standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        standardTile("powered", "device.powered", width: 2, height: 2, inactiveLabel: false) {
			state "powerOn", label: "Power On", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "powerOff", label: "Power Off", icon: "st.switches.switch.off", backgroundColor: "#ffa81e"
		}
		standardTile("configure", "device.configure", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}
        valueTile("voltage", "device.voltage", width: 2, height: 2) {
            state "val", label:'${currentValue}v', unit:"", defaultState: true
        }
        valueTile("voltageCounts", "device.voltageCounts", width: 2, height: 2) {
            state "val", label:'${currentValue}', unit:"", defaultState: true
        }
		main (["switch"])
		details(["switch", "contact", "voltage", "powered", "refresh","configure"])
	}
}

def parse(String description) {
	log.debug "description is: ${description}"

	def result = null
	def cmd = zwave.parse(description, [0x20: 1, 0x84: 1, 0x30: 1, 0x70: 1, 0x31: 5])
    
    log.debug "command value is: $cmd.CMD"
    
    if (cmd.CMD == "7105") {				//Mimo sent a power loss report
    	log.debug "Device lost power"
    	sendEvent(name: "powered", value: "powerOff", descriptionText: "$device.displayName lost power")
    } else {
    	sendEvent(name: "powered", value: "powerOn", descriptionText: "$device.displayName regained power")
    }
    //log.debug "${device.currentValue('contact')}" // debug message to make sure the contact tile is working
	if (cmd) {
    	log.debug "Creating Event :$cmd.CMD"
		result = createEvent(zwaveEvent(cmd))
	}
	log.debug "Parse returned ${result?.descriptionText} $cmd.CMD"
	return result
}

def updated() {
	log.debug "Settings Updated..."
    configure()
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
	log.debug "zwaveEvent ConfigurationReport: '${cmd}'"
    [:]
}

//notes about zwaveEvents:
// these are special overloaded functions which MUST be returned with a map similar to (return [name: "switch", value: "on"])
// not doing so will produce a null on the parse function, this will mess you up in the future.
// Perhaps can use 'createEvent()' and return that as long as a map is inside it.
def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) { 
log.debug "switchBinaryReport ${cmd}"
    if (cmd.value) // if the switch is on it will not be 0, so on = true
    {
		return [name: "switch", value: "on"] // change switch value to on
    }
    else // if the switch sensor report says its off then do...
    {
		return [name: "switch", value: "off"] // change switch value to off
    }
       
}

// working on next for the analogue and digital stuff.
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) // basic set is essentially our digital sensor for SIG1
{
	log.debug "sent a BasicSet ${cmd}"
    //this may not be working, but not using this 
    zwave.sensorMultilevelV5.sensorMultilevelGet().format()
    return [name: "powered", value: "powerOn"]
}
    
def zwaveEvent(physicalgraph.zwave.commands.sensorbinaryv1.SensorBinaryReport cmd)
{
	log.debug "sent a sensorBinaryReport ${cmd}"
    if (cmd.sensorValue) // if the switch is on it will not be 0, so on = true
    {
		return [name: "contact", value: "open"] // change switch value to on
    }
    else // if the switch sensor report says its off then do...
    {
		return [name: "contact", value: "closed"] // change switch value to off
    }
}

    
def zwaveEvent (physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd) // sensorMultilevelReport is used to report the value of the analog voltage for SIG1
{
	log.debug "sent a SensorMultilevelReport"
	def ADCvalue = cmd.scaledSensorValue
    sendEvent(name: "voltageCounts", value: ADCvalue)
   
    def mapp = CalculateVoltage(cmd.scaledSensorValue)
    
    if (mapp.value > 4)
    {
    	sendEvent(name: "voltage", value: mapp.value)
    	return [name: "contact", value: "open"]
    }
    if (mapp.value < 4)
    {
    	sendEvent(name: "voltage", value: mapp.value)
    	return [name: "contact", value: "closed"]
    }
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles all Z-Wave commands we aren't interested in
     log.debug("Un-parsed Z-Wave message ${cmd}")
	[:]
}

def CalculateVoltage(ADCvalue)
{
	 def map = [:]
     
     def volt = (((1.5338*(10**-16))*(ADCvalue**5)) - ((1.2630*(10**-12))*(ADCvalue**4)) + ((3.8111*(10**-9))*(ADCvalue**3)) - ((4.7739*(10**-6))*(ADCvalue**2)) + ((2.8558*(10**-3))*(ADCvalue)) - (2.2721*(10**-2)))

    //def volt = (((3.19*(10**-16))*(ADCvalue**5)) - ((2.18*(10**-12))*(ADCvalue**4)) + ((5.47*(10**-9))*(ADCvalue**3)) - ((5.68*(10**-6))*(ADCvalue**2)) + (0.0028*ADCvalue) - (0.0293))
	//log.debug "$cmd.scale $cmd.precision $cmd.size $cmd.sensorType $cmd.sensorValue $cmd.scaledSensorValue"
	def voltResult = volt.round(1)// + "v"    
    
	map.name = "voltage"
    map.value = voltResult
    map.unit = "v"
    return map
}
	

def configure() {
    def triggerMapping = Param3.toInteger()
    def lowerhigh = Param4.toInteger()
    def lowerlow = Param5.toInteger()
    def higherhigh = Param6.toInteger()
    def higherlow = Param7.toInteger()
    def inputFlags = (Param8_1.toInteger()*2) + Param8_0.toInteger()
    def periodicInterval = Param9.toInteger()
    def momentartRelay = Param11.toInteger()
	def cmds = []
    
    //Config Parameters
    if (Param2 == 1)
    {
    	log.debug "Param2:Clear"
    	cmds << zwave.configurationV1.configurationSet(parameterNumber: 2, size: 1, configurationValue:[1]).format() // clear pulse meter counts
    }    
    log.debug "Param3:$triggerMapping"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, configurationValue:[triggerMapping]).format() // sig 1 triggers relay //no trigger
    log.debug "Param4:$lowerhigh"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, configurationValue:[lowerhigh]).format() // lower threshold, high
    log.debug "Param5:$lowerlow"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 5, size: 1, configurationValue:[lowerlow]).format() // lower threshold, low
    log.debug "Param6:$higherhigh"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 6, size: 1, configurationValue:[higherhigh]).format() // upper threshold, high
    log.debug "Param7:$higherlow"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 7, size: 1, configurationValue:[higherlow]).format() // upper threshold, low
    log.debug "Param8:$inputFlags"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, configurationValue:[inputFlags]).format() // set to analog, below bounds
    log.debug "Param9:$periodicInterval"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, configurationValue:[periodicInterval]).format() // disable periodic reports //enable 10 sec
    log.debug "Param11:$momentartRelay"
    cmds << zwave.configurationV1.configurationSet(parameterNumber: 11, size: 1, configurationValue:[momentartRelay]).format() // momentary relay //no delay
    
    //Association Groups
    if (Group1 == 1) 
    {
    	log.debug "Group1:On" 
		cmds << zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:zwaveHubNodeId).format()	//subscribe to basic sets on sig1
    }
    else
    {
    	log.debug "Group1:Off"
    	cmds << zwave.associationV1.associationRemove(groupingIdentifier:1, nodeId:zwaveHubNodeId).format()
    }
    if (Group2 == 1) 
    {
    	log.debug "Group2:On"
		cmds << zwave.associationV1.associationSet(groupingIdentifier:2, nodeId:zwaveHubNodeId).format()	//periodic multilevel reports on sig1
    }
    else
    {
    	log.debug "Group2:Off"
    	cmds << zwave.associationV1.associationRemove(groupingIdentifier:2, nodeId:zwaveHubNodeId).format()
    }
    if (Group3 == 1) 
    {
    	log.debug "Group3:On"
		cmds << zwave.associationV1.associationSet(groupingIdentifier:3, nodeId:zwaveHubNodeId).format()	//power disconnect
    }
    else
    {
    	log.debug "Group3:Off"
    	cmds << zwave.associationV1.associationRemove(groupingIdentifier:3, nodeId:zwaveHubNodeId).format()	
    }
    if (Group4 == 1) 
    {
    	log.debug "Group4:On"
		cmds << zwave.associationV1.associationSet(groupingIdentifier:4, nodeId:zwaveHubNodeId).format()	//subscribe to binary report on sig1
    }
    else
    {
    	log.debug "Group4:Off"
    	cmds << zwave.associationV1.associationRemove(groupingIdentifier:5, nodeId:zwaveHubNodeId).format()
    }
    if (Group5 == 1) 
    {
    	log.debug "Group5:On"
		cmds << zwave.associationV1.associationSet(groupingIdentifier:5, nodeId:zwaveHubNodeId).format()	//subscribe to pulse counts on sig1
    }
    else
    {
    	log.debug "Group5:Off"
    	cmds << zwave.associationV1.associationRemove(groupingIdentifier:5, nodeId:zwaveHubNodeId).format()
    }
    
    log.debug "Requesting switch binary report"
    cmds << zwave.switchBinaryV1.switchBinaryGet().format()
    
	log.debug "Requesting multisensor report"
   	cmds << zwave.sensorMultilevelV5.sensorMultilevelGet().format()

    delayBetween(cmds, 500)
}

def on() {
	delayBetween([
		zwave.basicV1.basicSet(value: 0xFF).format(),	// physically changes the relay from on to off and requests a report of the relay
        refresh()// to make sure that it changed (the report is used elsewhere, look for switchBinaryReport()
       ])
}

def off() {
	delayBetween([
		zwave.basicV1.basicSet(value: 0x00).format(), // physically changes the relay from on to off and requests a report of the relay
        refresh()// to make sure that it changed (the report is used elsewhere, look for switchBinaryReport()
	])
}

def refresh() {
log.debug "REFRESH!"

    def cmds = []    
    log.debug "Requesting multisensor report"
    cmds << zwave.switchBinaryV1.switchBinaryGet().format()
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet().format()
    delayBetween(cmds, 500)

}
