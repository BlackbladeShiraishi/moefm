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
    player.eventBus().ofType(Player.ResumeEvent).subscribe {
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
    player.eventBus().ofType(Player.ResumeEvent).take(5).subscribe {
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
    player.eventBus().ofType(Player.ResumeEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playList.move(5, 0)
    assert playService.location == 0
    player.eventBus().ofType(Player.ResumeEvent).subscribe(playCurrentSong)
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
    player.eventBus().ofType(Player.ResumeEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playList.move(3, 5)
    assert playService.location == 4
    player.eventBus().ofType(Player.ResumeEvent).subscribe(playCurrentSong)
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
    player.eventBus().ofType(Player.ResumeEvent).take(5).subscribe(playCurrentSong)
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
    player.eventBus().ofType(Player.ResumeEvent).subscribe(playCurrentSong)
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
    player.eventBus().ofType(Player.ResumeEvent).take(5).subscribe(playCurrentSong)
    playService.play()

    assert player.counter == 5
    assert playService.location == 5
    playService.setLocation(6)
    assert playService.location == 6
    assert playService.state == PlayService.State.Playing
    player.eventBus().ofType(Player.ResumeEvent).subscribe(playCurrentSong)
    playCurrentSong()

    then:
    playService.location == 10
    player.counter == 9
  }

}
