package com.github.blackbladeshiraishi.fm.moe.business.business.impl

import com.github.blackbladeshiraishi.fm.moe.domain.entity.Song
import groovy.transform.CompileDynamic
import spock.lang.Specification

@CompileDynamic
class DefaultPlayListSpecification extends Specification {

  def "add 1 song to play list"() {
    setup:
    def playList = new DefaultPlayList()

    when:
    playList.add(new Song(id: 0, title: "Song 0"))

    then:
    playList.size() == 1
    playList.get(0).with {
      id == 0
      title == "Song 0"
    }
  }

  def "add 10 song to play list"() {
    setup:
    def playList = new DefaultPlayList()

    when:
    (0..9).each {
      println "adding song $it"
      playList.add(new Song(id: it, title: "Song $it"))
    }

    then:
    playList.size() == 10
    (0..9).each {index ->
      playList.get(index).with {
        id == index
        title == "song $id" as String
      }
    }
  }
}
