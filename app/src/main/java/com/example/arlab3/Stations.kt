package com.example.arlab3

import android.location.Location
import android.location.LocationManager

object Stations {
    val stationList: MutableList<Station> = java.util.ArrayList()

    init{
        val matinkyla = Station("Matinkyla",Location(LocationManager.NETWORK_PROVIDER))
        matinkyla.pos.latitude = 60.159909
        matinkyla.pos.longitude = 24.737914
        stationList.add(matinkyla)

        val niittykumpu = Station("Niittykumpu",Location(LocationManager.NETWORK_PROVIDER))
        niittykumpu.pos.latitude = 60.170498
        niittykumpu.pos.longitude = 24.763486
        stationList.add(niittykumpu)

        val urheilupuisto =Station("Urheilupuisto",Location(LocationManager.NETWORK_PROVIDER))
        urheilupuisto.pos.latitude = 60.174559
        urheilupuisto.pos.longitude = 24.779717
        stationList.add(urheilupuisto)

        val tapiola = Station("Tapiola",Location(LocationManager.NETWORK_PROVIDER))
        tapiola.pos.latitude = 60.175196
        tapiola.pos.longitude = 24.805999
        stationList.add(tapiola)

        val aalto = Station("Aalto-yliopisto",Location(LocationManager.NETWORK_PROVIDER))
        aalto.pos.latitude = 60.184811
        aalto.pos.longitude = 24.822811
        stationList.add(aalto)

        val keilaniemi = Station("Keilaniemi",Location(LocationManager.NETWORK_PROVIDER))
        keilaniemi.pos.latitude = 60.175915
        keilaniemi.pos.longitude = 24.89056
        stationList.add(keilaniemi)

        val koivusaari = Station("Koivusaari",Location(LocationManager.NETWORK_PROVIDER))
        koivusaari.pos.latitude = 60.162854
        koivusaari.pos.longitude = 24.855879
        stationList.add(koivusaari)

        val lauttasaari = Station("Lauttasaari",Location(LocationManager.NETWORK_PROVIDER))
        lauttasaari.pos.latitude = 60.159801
        lauttasaari.pos.longitude = 24.880148
        stationList.add(lauttasaari)

        val ruoholahti = Station("Ruoholahti",Location(LocationManager.NETWORK_PROVIDER))
        ruoholahti.pos.latitude = 60.163130
        ruoholahti.pos.longitude = 24.914853
        stationList.add(ruoholahti)

        val kamppi = Station("Kamppi",Location(LocationManager.NETWORK_PROVIDER))
        kamppi.pos.latitude = 60.168865
        kamppi.pos.longitude = 24.929819
        stationList.add(kamppi)

        val rautatieasema = Station("Rautatieasema",Location(LocationManager.NETWORK_PROVIDER))
        rautatieasema.pos.latitude = 60.170835
        rautatieasema.pos.longitude = 24.943689
        stationList.add(rautatieasema)

        val yliopisto = Station("Helsingin Yliopisto",Location(LocationManager.NETWORK_PROVIDER))
        yliopisto.pos.latitude = 60.171038
        yliopisto.pos.longitude = 24.948238
        stationList.add(yliopisto)

        val hakaniemi = Station("Hakaniemi",Location(LocationManager.NETWORK_PROVIDER))
        hakaniemi.pos.latitude = 60.179397
        hakaniemi.pos.longitude = 24.949943
        stationList.add(hakaniemi)

        val sornainen = Station("Sörnäinen",Location(LocationManager.NETWORK_PROVIDER))
        sornainen.pos.latitude = 60.187797
        sornainen.pos.longitude = 24.960571
        stationList.add(sornainen)

        val kalasatama = Station("Kalasatama",Location(LocationManager.GPS_PROVIDER))
        kalasatama.pos.latitude = 60.187422
        kalasatama.pos.longitude = 24.977622
        stationList.add(kalasatama)

        val kulosaari = Station("Kulosaari",Location(LocationManager.NETWORK_PROVIDER))
        kulosaari.pos.latitude = 60.188784
        kulosaari.pos.longitude = 25.007977
        stationList.add(kulosaari)

        val herttoniemi = Station("Herttniemi",Location(LocationManager.NETWORK_PROVIDER))
        herttoniemi.pos.latitude = 60.194907
        herttoniemi.pos.longitude = 25.031194
        stationList.add(herttoniemi)

        val siilitie = Station("Siilitie",Location(LocationManager.NETWORK_PROVIDER))
        siilitie.pos.latitude = 60.205422
        siilitie.pos.longitude = 25.044573
        stationList.add(siilitie)

        val itis = Station("Itäkeskus",Location(LocationManager.NETWORK_PROVIDER))
        itis.pos.latitude = 60.210050
        itis.pos.longitude = 25.077431
        stationList.add(itis)

        val myllypuro = Station("Myllypuro",Location(LocationManager.NETWORK_PROVIDER))
        myllypuro.pos.latitude = 60.225111
        myllypuro.pos.longitude = 25.075619
        stationList.add(myllypuro)

        val kontula = Station("Kontula",Location(LocationManager.NETWORK_PROVIDER))
        kontula.pos.latitude = 60.235771
        kontula.pos.longitude = 25.083148
        stationList.add(kontula)

        val mellunmaki = Station("Mellunmäki",Location(LocationManager.NETWORK_PROVIDER))
        mellunmaki.pos.latitude = 60.238961
        mellunmaki.pos.longitude = 25.110501
        stationList.add(mellunmaki)

        val puotila = Station("Puotila",Location(LocationManager.NETWORK_PROVIDER))
        puotila.pos.latitude = 60.214545
        puotila.pos.longitude = 25.094939
        stationList.add(puotila)

        val rastila = Station("Rastila",Location(LocationManager.NETWORK_PROVIDER))
        rastila.pos.latitude = 60.205479
        rastila.pos.longitude = 25.120278
        stationList.add(rastila)

        val vuosaari = Station("Vuosaari",Location(LocationManager.NETWORK_PROVIDER))
        vuosaari.pos.latitude = 60.207146
        vuosaari.pos.longitude = 25.142508
        stationList.add(vuosaari)
    }
}