package com.github.blackbladeshiraishi.fm.moe.business.business.impl

import com.github.blackbladeshiraishi.fm.moe.business.business.PlayService
import com.github.blackbladeshiraishi.fm.moe.business.business.Player
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import groovy.transform.CompileDynamic
import spock.lang.Specification

@CompileDynamic
class PlayServiceSpecification extends Specification {

  def "play all in play list"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    List<Song> songPlayed = []
    player.eventBus().ofType(Player.PlayEvent).subscribe {
      println "location: ${playService.location}"
      songPlayed.add(player.song)
      player.playCompletedNow()
    }

    when:
    assert player.counter == 0
    playService.play()

    then:
    playService.location == 10
    player.counter == 10
    songPlayed.size() == 10
    (0..9).each {index ->
      songPlayed[index].with {
        assert id == index
        assert title == "song $index" as String
      }
    }
  }

  def "play to No5"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)


    when:
    assert player.counter == 0
    player.eventBus().ofType(Player.PlayEvent).take(5).subscribe {
      println "location: ${playService.location}"
      player.playCompletedNow()
    }
    playService.play()

    then:
    playService.location == 5
    player.counter == 5
  }

  def "move No5(when playing it) to No0"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    def playCurrentSong = {
      println "location: ${playService.location}"
      player.playCompletedNow()
    }

    when:
    assert player.counter == 0
    player.eventBus().ofType(Player.PlayEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playList.move(5, 0)
    assert playService.location == 0
    player.eventBus().ofType(Player.PlayEvent).subscribe(playCurrentSong)
    playCurrentSong()

    then:
    playService.location == 10
    player.counter == 15
  }

  def "play to No5, move No3 to No5, play again"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    def playCurrentSong = {
      println "location: ${playService.location}"
      player.playCompletedNow()
    }

    when:
    assert player.counter == 0
    player.eventBus().ofType(Player.PlayEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playList.move(3, 5)
    assert playService.location == 4
    player.eventBus().ofType(Player.PlayEvent).subscribe(playCurrentSong)
    playCurrentSong()

    then:
    playService.location == 10
    player.counter == 11
  }

  def "play to No5, move No3 & No4 to No7 & No8, play again"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    def playCurrentSong = {
      println "location: ${playService.location}"
      player.playCompletedNow()
    }

    when:
    assert player.counter == 0
    player.eventBus().ofType(Player.PlayEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    def song3 = playList.get(3)
    def song4 = playList.get(4)
    assert playList.move(4, 8).is(song4)
    assert playList.move(3, 7).is(song3)
    assert playList.get(7).is(song3)
    assert playList.get(8).is(song4)
    assert playService.location == 3
    player.eventBus().ofType(Player.PlayEvent).subscribe(playCurrentSong)
    playCurrentSong()

    then:
    playService.location == 10
    player.counter == 12
  }

  def "play to No5, skip No5 and play again"() {
    setup:
    def playList = new DefaultPlayList()
    (0..9).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    def playCurrentSong = {
      println "location: ${playService.location}"
      player.playCompletedNow()
    }


    when:
    assert player.counter == 0
    player.eventBus().ofType(Player.PlayEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playService.setLocation(6)
    assert playService.location == 6
    assert playService.state == PlayService.State.Playing
    player.eventBus().ofType(Player.PlayEvent).subscribe(playCurrentSong)
    playCurrentSong()

    then:
    playService.location == 10
    player.counter == 9
  }

  def "send events when play"() {
    given:
    def playList = new DefaultPlayList()
    (0..2).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    List<PlayService.Event> events = []
    playService.eventBus().subscribe{ events << it}

    assert events.isEmpty()
    assert playService.state == PlayService.State.Pausing

    when: "start play"
    playService.play()
    then:
    playService.state == PlayService.State.Playing
    events.size() == 1
    events[0].with isPlayEvent(0)

    when: "1st play completed"
    player.playCompletedNow()
    then:
    events.size() == 4
    events[1].with isPauseEvent(0)
    events[2].with isLocationChangeEvent(0, 1)
    events[3].with isPlayEvent(1)

    when: "2ed play completed"
    player.playCompletedNow()
    then:
    events.size() == 7
    events[4].with isPauseEvent(1)
    events[5].with isLocationChangeEvent(1, 2)
    events[6].with isPlayEvent(2)

    when: "3rd(last) play completed"
    player.playCompletedNow()
    then:
    events.size() == 9
    events[7].with isPauseEvent(2)
    events[8].with isLocationChangeEvent(2, 3)

    playService.location == 3
    playService.state == PlayService.State.Pausing
    player.counter == 3
  }

  def "change location when playing"() {
    given:
    def playList = new DefaultPlayList()
    (0..2).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    List<PlayService.Event> events = []
    playService.eventBus().subscribe{ events << it}

    assert events.isEmpty()
    assert playService.state == PlayService.State.Pausing

    when: "start play"
    playService.play()
    then:
    playService.state == PlayService.State.Playing
    events.size() == 1
    events[0].with isPlayEvent(0)

    when: "change location to 2"
    playService.setLocation(2)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 4
    events[1].with isPauseEvent(0)
    events[2].with isLocationChangeEvent(0, 2)
    events[3].with isPlayEvent(2)

    when: "change back location 0"
    playService.setLocation(0)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 7
    events[4].with isPauseEvent(2)
    events[5].with isLocationChangeEvent(2, 0)
    events[6].with isPlayEvent(0)

    when: "change location to 3(last)"
    playService.setLocation(3)
    then:
    playService.state == PlayService.State.Pausing
    events.size() == 9
    events[7].with isPauseEvent(0)
    events[8].with isLocationChangeEvent(0, 3)

    playService.location == 3
    player.counter == 0
  }

  def "skip to next and skip to previous"() {
    given:
    def playList = new DefaultPlayList()
    (0..2).each {
      playList.add(new Song(id: it, title: "song $it"))
    }
    def player = new MockPlayer()
    def playService = new DefaultPlayService(playList, player)
    List<PlayService.Event> events = []
    playService.eventBus().subscribe{ events << it}

    assert events.isEmpty()
    assert playService.state == PlayService.State.Pausing

    when: "start play"
    playService.play()
    then:
    playService.state == PlayService.State.Playing
    events.size() == 1
    events[0].with isPlayEvent(0)

    when: "change location to 1(skip to next)"
    playService.setLocation(1)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 4
    events[1].with isPauseEvent(0)
    events[2].with isLocationChangeEvent(0, 1)
    events[3].with isPlayEvent(1)

    when: "change back location 2(skip to next)"
    playService.setLocation(2)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 7
    events[4].with isPauseEvent(1)
    events[5].with isLocationChangeEvent(1, 2)
    events[6].with isPlayEvent(2)

    when: "change location to 1(skip to previous)"
    playService.setLocation(1)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 10
    events[7].with isPauseEvent(2)
    events[8].with isLocationChangeEvent(2, 1)
    events[9].with isPlayEvent(1)

    when: "change back location 2(skip to next)"
    playService.setLocation(2)
    then:
    playService.state == PlayService.State.Playing
    events.size() == 13
    events[10].with isPauseEvent(1)
    events[11].with isLocationChangeEvent(1, 2)
    events[12].with isPlayEvent(2)

    when: "change location to 3(skip to last)"
    playService.setLocation(3)
    then:
    playService.state == PlayService.State.Pausing
    events.size() == 15
    events[13].with isPauseEvent(2)
    events[14].with isLocationChangeEvent(2, 3)

    when: "change location to 2(skip to previous from last)"
    playService.setLocation(2)
    then:
    playService.state == PlayService.State.Pausing
    events.size() == 16
    events[15].with isLocationChangeEvent(3, 2)

    when: "start play"
    playService.play()
    then:
    playService.state == PlayService.State.Playing
    events.size() == 17
    events[16].with isPlayEvent(2)

    when: "play complete"
    player.playCompletedNow()
    then:
    playService.state == PlayService.State.Pausing
    events.size() == 19
    events[17].with isPauseEvent(2)
    events[18].with isLocationChangeEvent(2, 3)

    playService.location == 3
    player.counter == 1
  }

  Closure<Boolean> isPauseEvent(int assertLocation) {
    return {
      delegate.class == PlayService.PauseEvent &&
      location == assertLocation &&
      state == PlayService.State.Pausing
    }
  }

  Closure<Boolean> isPlayEvent(int assertLocation) {
    return {
      delegate.class == PlayService.PlayEvent &&
      location == assertLocation &&
      state == PlayService.State.Playing
    }
  }

  Closure<Boolean> isLocationChangeEvent(int assertOldLocation, int assertNewLocation) {
    return {
      delegate.class == PlayService.LocationChangeEvent &&
      oldLocation == assertOldLocation &&
      newLocation == assertNewLocation &&
      location == assertNewLocation &&
      state == PlayService.State.Pausing //only change at PAUSING state
    }
  }

}
