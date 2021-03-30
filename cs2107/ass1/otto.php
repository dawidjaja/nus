<?php
    
    class Hacker {
        private $show_flag = TRUE;
        private $username = 'hackerotto';
        
        public function toString() {
            $str = $this->username;
            // Aha you shall not get the flag easily.
            return $str;
        }

        public function set() {
          $this->show_flag = FALSE;
          $this->username = 'hacker1337';
        }
    }
    function unserialize_safe($str) {        
        return unserialize($str, ['allowed_classes' => ['Hacker']]);
    }

    if(isset($_COOKIE['hackers'])) {
        $serialized_hackers = $_COOKIE['hackers'];
        echo 'bebong';
        echo $serialized_hackers;

        //$hackers = array_map('unserialize_safe', $serialized_hackers);
        $hackers = array_map('unserialize_safe', $xxx);
        echo $hackers;
    } else {
        $a = new Hacker;
        $a->set();
        $hackers = [$a, new Hacker];
        // http://php.net/manual/en/function.serialize.php
        // Maybe this will help you understand serialization.
        $serialized_hackers = implode(' ', array_map('serialize', $hackers));        
        echo '\n';
        echo urlencode($serialized_hackers);
        echo '\asu';
    }
?>

<html>
<head>
    <meta charset="UTF-8">
    <title>Secure site 10000</title>
</head>
<body>
<h1>The Hacker Wall of Fame:</h1>
<?php foreach($hackers as $hacker): ?>
    <li><?= $hacker->toString() ?></li>
<?php endforeach; ?>
<p /><a href="?source=1">Show</a>/<a href="/">hide</a> page source<p />
<?php if (isset($_GET['source'])) highlight_file(__FILE__); ?>
</body>
</html>
