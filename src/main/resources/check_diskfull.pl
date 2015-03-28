#!/usr/bin/perl5.60 -w

# by lixh 11/24/04
# check if the disk on the middle server is full
# (right now the middle server is swarna.ncsa.uiuc.edu)

use CGI qw/:standard/;
use Expect;
use strict;

my $date = `date`;
print STDERR "today is $date \n";

# use expect to run the command
my $cmd_filehandle = Expect->spawn("df -k");

# gather the output of the command
# ignore the first line
 my $line = <$cmd_filehandle>;

while ($line = <$cmd_filehandle>) {
    	$line =~ s/  */ /g;
	my @output = split(/ /,$line);
# 	print STDERR "$output[5] \n";
	if ($output[5] > 30) {
        	my $dir = $output[6];
		$dir =~ s/\n//g;
		my $used = $output[5];
 		print STDERR "warning: $used% of $dir is used \n";
	}
 } 

# close the filehandle to the command
# $cmd_filehandle->soft_close();
# $cmd_filehandle->hard_close();
close ($cmd_filehandle);

