#include <EEPROM.h>
#include <Keyboard.h>

int BACKSPACE = 8;
unsigned long HOLD_TERM = 200;
unsigned long MAX_LONG = 4294967295;
int button_pins[2] = { 2, 3 };
unsigned long pin_state[2] = { 0, 0 };
boolean hold_action_done[2] = { false, false };

int tap_key_code[2] = { '0', '1' };
int hold_key_code[2] = { BACKSPACE, ' ' };

void setup() {
  int i;

  for (i = 0; i < 2; i++) {
    pinMode(button_pins[i], INPUT_PULLUP);
//    digitalWrite(button_pins[i], HIGH);
  }
  
  Keyboard.begin();
}

bool debounce(unsigned long t_now, unsigned long t_prev) {
  unsigned long diff;

  diff = t_now - t_prev; // need to check for underflow?

  if (diff <= 20) return true;
  else return false;
}

void loop() {
  unsigned long tick_now = millis();
  int i;

  // since we use non zero to indicate pressed state, we need
  // to handle the edge case where millis() returns 0
  if (tick_now == 0) {
    tick_now = 1;
  }

  for (i = 0; i < 2; i++) {
    int key_state = 0;

    // ignore state change for pin if in debounce period
    if (pin_state[i] != 0) {
      if (debounce(tick_now, pin_state[i]) == true) {
        continue;
      }
    }
      
    if (digitalRead(button_pins[i]) == HIGH) {
      // button is up
      if (pin_state[i] != 0) {
        // just released; need to reset if held, or send tap keycode
        if(hold_action_done[i]) {
          hold_action_done[i] = false;
        } else {
          Keyboard.write(tap_key_code[i]);
        }
        
        pin_state[i] = 0;
      }
    } else {
      // button is down
      if (pin_state[i] == 0) {
        // just pressed; need to record timestamp
        pin_state[i] = tick_now;
      } else if (! hold_action_done[i]) {
        int press_time = pin_state[i];
        if (tick_now < press_time) {
          tick_now += MAX_LONG;
        }
        
        if (tick_now - press_time > HOLD_TERM) {      
          Keyboard.write(hold_key_code[i]);
          hold_action_done[i] = true;
        }
      }
    }
  }

  delay(1);
}
