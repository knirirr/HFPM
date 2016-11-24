#!/usr/bin/env ruby

class RoundRobinPairsGenerator
  attr_reader :competitors, :full_size
  def initialize(n)
    @competitors = (1..n).to_a
    @full_size =  (n*n - n) / 2
  end
  def pairings
    @pairings ||= compute_pairings
  end
  private
  def more_tired(played, pair)
    appearances = played.flatten
    pair.sort_by {|comp| appearances.select { |e| e == comp }.length}.last
  end
  def compute_pairings
    pairings = []
    picks = competitors.dup
    while pairings.length < full_size
      trial_pair = picks[0..1].sort
      if pairings.include?(trial_pair)
        more_tired = more_tired(pairings, trial_pair)
        picks.delete(more_tired)
        picks << more_tired
      else
        pairings << trial_pair
        picks << picks.shift
        picks << picks.shift
      end
    end
    pairings
  end
end

bouts = [3,8]
#4.upto(10) do |x|
bouts.each do |x|
    puts "For n = #{x}"
    RoundRobinPairsGenerator.new(x).pairings.each { |p| p p }
    puts
end
