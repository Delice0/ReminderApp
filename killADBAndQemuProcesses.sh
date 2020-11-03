if ps -W | grep qemu > /dev/null
then
	echo "QEMU process is active.. killing it now"
	kill \$!
else
	echo "No QEMU process is running.. exiting script"
fi

if ps -W | grep adb > /dev/null
then
	echo "ADB process is running.. killing it now"
	kill \$!
else
	echo "No ADB process is running.. exiting script"
fi
