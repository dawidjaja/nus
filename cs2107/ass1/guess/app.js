const express = require('express')
const app = express()
const port = 8080

app.use(express.static('public'))
const session = require('express-session')
const bodyParser = require('body-parser')
app.use(bodyParser.urlencoded({ extended: false }))

app.use(session({
  secret: 'whatever this is not rly important',
  cookie: { secure: false, maxAge: 60000 }
}))

const rand_num = () => {
  const x = Math.random()
  console.log(x)
  return x.toString()
    .substr(2)
    .padEnd(13, '0')
}

const rand_nums = () => {
  nums = []
  for (i = 0; i < 10; ++i) nums.push(rand_num())
  return nums
}

const reseed = () => {
  for (i = 0; i < 400; ++i) rand_num()
}

app.post('/submit', (req, res) => {
  console.log(req.session)
  if (req.session.attempts) {
    req.session.attempts++

    if (req.session.attempts > 10) {
      reseed()
      req.session.nums = rand_nums()
      req.session.attempts = 1
    }
  }
  else {
    req.session.attempts = 1
    req.session.nums = rand_nums()
  }

  guess = req.body.guess
  correct = req.session.nums[req.session.attempts - 1]
  if (guess === correct) {
    res.json({ correct: true, flag: process.env.FLAG })
  }
  else {
    res.json({ correct: false, answer: correct, attempts: req.session.attempts })
  }
})

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})
