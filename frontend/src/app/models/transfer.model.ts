export interface Transfer {
  idTransfer: number;
  sender: string; // Nadawca przelewu
  recipient: string; // Odbiorca przelewu
  title: string;
  date: Date;
  amount: number;
  idAccount: number;
}