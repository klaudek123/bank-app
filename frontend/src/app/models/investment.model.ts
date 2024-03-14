export interface Investment {
  idInvestment: number;
  name: string;
  type: InvestmentType;
  amount: number;
  interestRate: number;
  startDate: Date;
  endDate: Date;
  status: InvestmentStatus;
  idAccount: number;
}

export enum InvestmentType {
  FUND = "FUND",
  GOLD = "GOLD",
  SILVER = "SILVER"
}

export enum InvestmentStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE"
}