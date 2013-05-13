#!/bin/bash

SCRIPT_DIR=$(dirname "$0")
SERVICE='craftbukkit*.jar'
#USERNAME="minecraft"
CPU_COUNT=2
BUKKIT="$SCRIPT_DIR/../target/lib/$SERVICE"
INVOCATION="java -Xmx1000M -Xms300M -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalPacing -XX:ParallelGCThreads=$CPU_COUNT -XX:+AggressiveOpts -jar $BUKKIT nogui"
MCPATH="$SCRIPT_DIR/../bukkit-testserver"

if [ ! -d "$MCPATH" ]; then
    mkdir -p "$MCPATH"
fi

ME=$(whoami)
as_user() {
    #if [ $ME == $USERNAME ] ; then
        bash -c "$1"
    #else
        #su - $USERNAME -c "$1"
    #fi
}

mc_start() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "Tried to start but $SERVICE was already running!"
    else
        echo "$SERVICE was not running... starting."
        cd "$MCPATH"
        as_user "cd "$MCPATH" && screen -dmS minecraft $INVOCATION"
        sleep 7
        if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
        then
            echo "$SERVICE is now running."
        else
            echo "Could not start $SERVICE."
        fi
    fi
}

mc_stop() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE is running... stopping."
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"save-all\"\015'"
        sleep 2
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"stop\"\015'"
        sleep 6
    else
        echo "$SERVICE was not running."
    fi
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE could not be shut down... still running."
    else
        echo "$SERVICE is shut down."
    fi
}

mc_save() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE is running... saving."
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"save-all\"\015'"
    else
        echo "$SERVICE was not running."
    fi
}

mc_reload() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE is running... reloading."
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"reload\"\015'"
    else
        echo "$SERVICE was not running."
    fi
}

mc_reload_or_start() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE was already running! Doing a reload now!"
        mc_reload
    else
        echo "$SERVICE was not running... starting."
        cd "$MCPATH"
        as_user "cd \"$MCPATH\" && screen -dmS minecraft $INVOCATION"
        sleep 7
        if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
        then
            echo "$SERVICE is now running."
        else
            echo "Could not start $SERVICE."
        fi
    fi
}

mc_ddidderr_admin() {
    if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
    then
        echo "$SERVICE is running... making ddidder to admin and reloading permissions."
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"pex user ddidderr group set admin\"\015'"
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"pex reload\"\015'"
    else
        echo "$SERVICE was not running."
    fi
}

case "$1" in
    start)
        echo "Starting Minecraft..."
        mc_start
        echo "DONE"
        ;;
    stop)
        echo "Stopping Minecraft..."
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"say SERVER SHUTTING DOWN!\"\015'"
        mc_stop
        echo "DONE"
        ;;
    restart)
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"say SERVER REBOOT IN 10 SECONDS.\"\015'"
        $0 stop
        sleep 1
        $0 start
        ;;
    reload)
        mc_reload
        ;;
    reload_or_start)
        echo "Starting or reloading Minecraft..."
        mc_reload_or_start
        echo "DONE"
        ;;
    ddidderr_admin)
        mc_ddidderr_admin
        ;;
    connected)
        as_user "screen -p 0 -S minecraft -X eval 'stuff \"who\"\015'"
        sleep 2s
        tac "$MCPATH"/server.log | grep -m 1 "Connected"
        ;;
    status)
        if ps ax | grep -v grep | grep -v -i SCREEN | grep "craftbukkit" > /dev/null
        then
            echo "$SERVICE is running."
        else
            echo "$SERVICE is not running."
        fi
        ;;
    save)
        mc_save
        ;;
    *)
        echo "Usage: /etc/init.d/minecraft {start|stop|restart|connected|status}"
        exit 1
        ;;
esac
